
package project1;

import project1.IoTScanner.IoTScanner;
import project1.IoTScanner.PortScanResult;
import project1.SecurityReport.ScanReport;
import project1.SecurityReport.ReportWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class IoTScannerApp {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println(
                    "Example: IoTScannerApp <Device-ip>");
            return;
        }

        String ipAddress = args[0];

        try {
            //Run the scan
            IoTScanner scanner = new IoTScanner();
            List<PortScanResult> results =
                    scanner.scan(ipAddress);

            //Obtain device details
            String macAddress = results.isEmpty()
                    ? "Unknown"
                    : results.get(0).getMacAddress();

            String manufacturer = results.isEmpty()
                    ? "Unknown manufacturer"
                    : results.get(0).getManufacturer();

            int totalScannedPorts = scanner.getScannedPorts();
            //Create the report

            ScanReport report = new ScanReport(
                    ipAddress,
                    macAddress,
                    manufacturer,
                    totalScannedPorts,
                    results
            );

            // 4. Write the JSON report
            ReportWriter writer =
                    new ReportWriter();

            Path reportPath = Path.of(
                    "Reports",
                    "Scan-Report-" + ipAddress.replace(':', '_')
                            + ".json"
            );

            writer.writeReport(report, reportPath);

            System.out.println(
                    "Scan report saved to: "
                            + reportPath.toAbsolutePath());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(
                    "MAC discovery was interrupted.");

        } catch (IOException e) {
            System.err.println(
                    "I/O error: " + e.getMessage());

        } catch (Exception e) {
            System.err.println(
                    "Scan failed: " + e.getMessage());
        }
    }
}
