package project1.IoTScanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScanResultTest {
    private  IoTScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new IoTScanner();
    }
    @Test
    void TestingScanResultSuccessfulWithoutTCPConnection(){
        PortChecker testChecker = ((host, port) -> port ==80 || port == 8008);
        IoTScanner scanner1 = new IoTScanner(testChecker);

        List<PortScanResult>results = scanner1.scan("localhost");

        assertEquals(2,results.size());
        boolean found = false;
        for (PortScanResult result:results){
            if (result.getPort() == 80){
                found = true;
            }
        }assertTrue(found);

        assertTrue(results.stream().anyMatch(result -> result.getPort() == 8008));
        assertFalse(results.stream().anyMatch(result->result.getPort() == 22));
    }

    @Test
    void TestingScanResultsEmptyIfNoPortsOpen(){
        PortChecker testChecker = (host,port) -> false;
        IoTScanner testScanner = new IoTScanner(testChecker);
        List<PortScanResult>results = testScanner.scan("localhost");

        assertTrue(results.isEmpty());
    }
    @Test
    void TestScanResultGettersReturnCorrectValues(){
        PortChecker testChecker = ((host, port) -> port == 22 || port ==443);
        IoTScanner testScanner = new IoTScanner(testChecker);
        List<PortScanResult>results = testScanner.scan("localhost");
        String hostName = results.getFirst().getHost();

        assertEquals("localhost",hostName);
        assertEquals(22,results.getFirst().getPort());
        assertEquals(443,results.getLast().getPort());
        assertTrue(results.getFirst().getService().contains("SSH"));
        assertTrue(results.getLast().getService().contains("HTTPS"));

        assertTrue(results.toString().contains("Host"));
    }

}
