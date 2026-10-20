package project1.IoTScanner;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScanReportTest {

    @Test

    void TestSuccessfulReportGeneration(){
        List<PortScanResult>results = List.of(new PortScanResult("192.25.6.45",22,"TCP-SSH"),
                new PortScanResult("192.25.6.45",80,"TCP-HTTP"),new PortScanResult("192.25.6.45",443,"TCP-HTTPS"),
                new PortScanResult("192.25.6.45",23,"TCP-Telnet"),
                new PortScanResult("192.25.6.45",1883,"TCP-MQTT"));

        ScanReport report = new ScanReport();
        String fullReport = report.generateReport("192.25.6.45",results);

        assertTrue(fullReport.contains("IoT SCAN REPORT"));
        assertTrue(fullReport.contains("192.25.6.45"));
        assertTrue(fullReport.contains("TCP-HTTP"));
        assertTrue(fullReport.contains("TCP-HTTPS"));
        assertTrue(fullReport.contains("TCP-SSH"));
        assertTrue(fullReport.contains("TCP-Telnet"));
        assertTrue(fullReport.contains("TCP-MQTT"));
        assertTrue(fullReport.contains("OPEN PORTS: "));
        assertTrue(fullReport.contains("TOTAL OPEN PORTS: 5"));

        //Testing correct Service with PortNumbers
        assertTrue(fullReport.contains("Port: 22 - Service: TCP-SSH"));
        assertTrue(fullReport.contains("Port: 80 - Service: TCP-HTTP"));
        assertTrue(fullReport.contains("Port: 443 - Service: TCP-HTTPS"));
        assertTrue(fullReport.contains("Port: 23 - Service: TCP-Telnet"));
        assertTrue(fullReport.contains("Port: 1883 - Service: TCP-MQTT"));
    }

    @Test

    void TestEmptyReportGenerationForNoOpenPorts(){
        List<PortScanResult>results = new ArrayList<>();

        ScanReport report = new ScanReport();
        String form = report.generateReport("192.25.6.45",results);

        assertTrue(results.isEmpty());
        assertTrue(form.contains("192.25.6.45"));
        assertTrue(form.contains("OPEN PORTS: "));
        assertTrue(form.contains("TOTAL OPEN PORTS: 0"));
        assertFalse(form.contains("SSH"));
    }
}
