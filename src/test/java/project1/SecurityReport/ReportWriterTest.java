package project1.SecurityReport;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import project1.IoTScanner.PortScanResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;

public class ReportWriterTest {

    @TempDir

    Path tempDir;

    @Test

    void TestReportWriterCreatesJsonFile()throws IOException{

        Map<Integer,String> ports = new HashMap<>();

        ports.put(22,"TCP - SSH");
        ports.put(80,"TCP - http");
        ports.put(443,"TCP https");

        List<PortScanResult> result = of();
        ScanReport report = new ScanReport("127.0.0.1","00:11:22:33:AA:BB","Test Manufacturer Ltd", ports.size(),result);

        Path jsonFile = tempDir.resolve("scan-report.json");

        ReportWriter writer  = new ReportWriter();

        writer.writeReport(report,jsonFile);
        //Make Sure file exists
        assertTrue(Files.exists(jsonFile));

        //Make sure file is not empty
        String json = Files.readString(jsonFile);
        assertFalse(json.isBlank());

        //Make sure all relevant content is in the file

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("127.0.0.1",root.get("target").getAsString());
        assertEquals("00:11:22:33:AA:BB",root.getAsJsonObject("device").get("macAddress").getAsString());
        assertEquals("Test Manufacturer Ltd",root.getAsJsonObject("device").get("manufacturer").getAsString());
        assertEquals(3,root.getAsJsonObject("summary").get("totalPortsChecked").getAsInt());
        assertEquals(0,root.getAsJsonObject("summary").get("openPorts").getAsInt());
    }
}
