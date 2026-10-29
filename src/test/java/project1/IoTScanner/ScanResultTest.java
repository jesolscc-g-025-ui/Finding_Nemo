package project1.IoTScanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project1.MacDiscovery.CommandRunner;
import project1.MacDiscovery.MacDiscovery;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScanResultTest {
    private  IoTScanner scanner;
    private PortChecker portChecker;
    private MacDiscovery Macdiscovery;
    private DeviceReachability deviceReachability;

    @Test
    void TestingScanResultSuccessfulWithoutTCPConnection() throws Exception {
        PortChecker testChecker =
                (host, port) -> port == 80 || port == 8008;


            IoTScanner testScanner = new IoTScanner(
                    testChecker,
                    createTestDiscovery(),
                    ipAddress -> false
            );

            List<PortScanResult> results = testScanner.scan("localhost");

            assertEquals(2, results.size());
            assertTrue(results.stream().anyMatch(r -> r.getPort() == 80));
            assertTrue(results.stream().anyMatch(r -> r.getPort() == 8008));
            assertFalse(results.stream().anyMatch(r -> r.getPort() == 22));
        }

    @Test
    void TestingScanResultsEmptyIfNoPortsOpen()throws  Exception{
        PortChecker testChecker = (host,port) -> false;

        IoTScanner testScanner = new IoTScanner(testChecker,createTestDiscovery(),ipAddress -> false);
        List<PortScanResult>results = testScanner.scan("localhost");

        assertTrue(results.isEmpty());
    }
    @Test
    void TestScanResultGettersReturnCorrectValues()throws Exception{
        PortChecker testChecker = ((host, port) -> port == 22 || port ==443);

        IoTScanner testScanner = new IoTScanner(testChecker,createTestDiscovery(),ipAddress -> false);
        List<PortScanResult>results = testScanner.scan("localhost");
        String hostName = results.getFirst().getHost();

        assertEquals("localhost",hostName);
        assertEquals(22,results.getFirst().getPort());
        assertEquals(443,results.getLast().getPort());
        assertTrue(results.getFirst().getService().contains("SSH"));
        assertTrue(results.getLast().getService().contains("HTTPS"));

        assertTrue(results.toString().contains("Host"));
    }

    private MacDiscovery createTestDiscovery(){
        CommandRunner fakeRunner = command -> {
            if (command.get(0).equalsIgnoreCase("ping")) {
                return " reply from 127.0.0.1: bytes=32 time<1ms";
            }
            if (command.get(0).equalsIgnoreCase("arp")) {
                return "127.0.0.1  00:11:22:33:AA:BB  dynamic";
            }
            return "";
        };
        return new  MacDiscovery(fakeRunner);
    }
}
