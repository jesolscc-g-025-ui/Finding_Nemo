package project1.IoTScanner;

import java.util.List;

public class ScanReport {

    public String generateReport(String host,List<PortScanResult>results) {

        StringBuilder report = new StringBuilder();

        report.append("=================================\n");
        report.append("         IoT SCAN REPORT\n");
        report.append("=================================\n");
        report.append("Device: ").append(host).append("\n\n");

        report.append("OPEN PORTS: \n");
        for (PortScanResult result : results) {
            report.append("Port: ").append(result.getPort()).append(" - ").append("Service: ").append(result.getService());
            report.append("\n");
        }

        report.append("\nTOTAL OPEN PORTS: ").append(results.size());
        report.append("\n--------------------------------------");
    return report.toString();
    }
}
