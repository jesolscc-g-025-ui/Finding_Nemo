package project1.SecurityReport;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import project1.IoTScanner.PortScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ScanReportTest {

    @Test

    void TestScanPortCreation(){
        String target = "127.0.0.1";
        String macAddress = "00:11:22:33:AA:BB";
        String manufacturer = "Test Manufacturer Ltd";
        Map<Integer,String> ports = new HashMap<>();
        ports.put(22,"TCP - SSH");
        ports.put(80,"TCP Http");

        List<PortScanResult>results = of();

        ScanReport report = new ScanReport(target,macAddress,manufacturer, ports.size(),results);

        assertNotNull(report);
    }

    @Test

    void TestScanReportContainsDeviceAndSummaryContent(){

        Map<Integer,String>ports = new HashMap<>();
        ports.put(22,"TCP - SSH");
        ports.put(80,"TCP - https");
        ports.put(443,"TCP - Https");

        List<PortScanResult>results = of();
        ScanReport report = new ScanReport("127.0.0.1","00:11:22:33:AA:BB","Test Manufacturer Ltd", ports.size(),results);

        String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(report);

        JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();

        assertEquals("127.0.0.1",root.get("target").getAsString());
        assertEquals("00:11:22:33:AA:BB",root.getAsJsonObject("device").get("macAddress").getAsString());
        assertEquals("Test Manufacturer Ltd",root.getAsJsonObject("device").get("manufacturer").getAsString());

        assertEquals("3",root.getAsJsonObject("summary").get("totalPortsChecked").getAsString());
        assertEquals(0,root.getAsJsonObject("summary").get("openPorts").getAsInt());

    }
}
