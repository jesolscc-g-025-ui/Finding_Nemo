package project1.IoTScanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IoTScannerTest {

    private IoTScanner scanner;

    @BeforeEach
    void setUp(){
        scanner = new IoTScanner();
        IoTScanner.addPorts();

    }

    @Test

    void TestingAddPortSuccessfully(){
        assertTrue(scanner.availablePorts().size()>1);
    }
    @ Test

//Testing Web and Remote Management Ports Successful Availability

    void TestingAvailablePortsContainSSH(){


        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(22));
        assertTrue(availablePorts.get(22).contains("SSH"));
    }
    @Test

    void TestingAvailablePortsContainHTTP(){
        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(80));
        assertTrue(availablePorts.get(80).contains("HTTP"));
    }

    @Test
    void TestingAvailablePortsContainTelNet(){
        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(23));
        assertTrue(availablePorts.get(23).contains("Telnet"));
    }
    @Test
    void TestingAvailablePortsContainRTSP(){
        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(554));
        assertTrue(availablePorts.get(554).contains("Real Time Streaming Protocol"));
    }
// Testing  Messaging and Application Protocol Ports Availability

    @Test
    void TestingAvailablePortsContainMQTT(){

        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(1883));
        assertTrue(availablePorts.get(1883).contains("Message Queuing Telemetry Transport"));
    }
    @Test
    void TestingAvailablePortsContainMQTTS(){

        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(8883));
        assertTrue(availablePorts.get(8883).contains("encrypting IoT messaging"));
    }
    @Test
    void TestingAvailablePortsContainAMQP(){

        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertTrue(availablePorts.containsKey(5672));
        assertTrue(availablePorts.get(5672).contains("Advanced Message Queuing Protocol"));
    }

    //Testing recognition of An Open Local Port

    @Test
    void portScannerShouldReturnOnlyOpenPorts(){
        // Simulate a Testcase Without Establishing Real-Life Connections

        PortChecker TestChecker = (host, port) ->
            port == 22 || port == 1883;

        IoTScanner tScanner = new IoTScanner(TestChecker);

        //results
        Map<Integer,String>results = tScanner.scan("192.168.5.27");

        assertEquals(2,results.size());
        assertTrue(results.containsKey(22));
        assertTrue(results.containsKey(1883));

    }
    @Test
    void TestingUnmodifiableListShouldNotBeChangeable(){
        Map<Integer,String>availablePorts = scanner.availablePorts();
        assertThrows(UnsupportedOperationException.class ,()-> availablePorts.put(9999,"testicle"));
    }
}
