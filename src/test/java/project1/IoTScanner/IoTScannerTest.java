package project1.IoTScanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IoTScannerTest {

    private final IoTScanner scanner = new IoTScanner();

    @Test
    void TestAddPorts(){ // Test if addPorts() works successfully
        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertFalse(availablePorts.isEmpty());
        assertTrue(availablePorts.containsKey(22));
        assertTrue(availablePorts.containsKey(21));
        assertTrue(availablePorts.containsKey(23));
        assertTrue(availablePorts.containsKey(80));
        assertTrue(availablePorts.containsKey(443));
    }

    @Test
    void TestUnmodifiableListReturnedForAvailablePorts(){
        //IllegalArgumentError should be thrown if attempt to add to List
        Map<Integer,String>availablePorts = scanner.availablePorts();

        assertThrows(UnsupportedOperationException.class ,() -> availablePorts.put(9000,"Testicles"));
    }

    @Test
    void TestIsPortOpenShouldReturnTrueForOpenPort()throws IOException {
        //If by chance all your ports on the testing device is closed
        //create a serverSocket to ask OS to open port do the listening for testing connection
        try    (ServerSocket socket = new ServerSocket(0)) {
            int port = socket.getLocalPort();
            assertTrue(scanner.isPortOpen("localhost", port));
        }
    }

    @Test
    void TestisPortOpenShouldReturnFalseForClosedPort()throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        assertFalse(scanner.isPortOpen("localhost", port));
    }

    @Test
    void TestingScanLogicWithoutRealTCPConnection(){
        PortChecker fakeChecker =
                (host, port) -> port == 80 || port == 1883;

        IoTScanner scanner1 = new IoTScanner(fakeChecker);
        assertTrue(scanner1.isPortOpen("localhost",80));
        assertTrue(scanner1.isPortOpen("localhost",1883));
        assertFalse(scanner1.isPortOpen("localhost",22));
        assertFalse(scanner1.isPortOpen("localhost",455));
    }
    @Test

    void TestIoTServiceIdentificationCorrelationWithPort(){
        Map<Integer,String>ports = scanner.availablePorts();

        assertEquals("TCP - SSH-Secure Shell (Remote login / CLI)",ports.get(22));
        assertEquals("TCP - FTP-Data(File transfer protocol)",ports.get(20));
        assertEquals("TCP - FTP-Control",ports.get(21));
        assertEquals("TCP - HTTP-Unencrypted Web Traffic",ports.get(80));
        assertEquals("TCP - HTTPS-Secure Web Management",ports.get(443));
        assertEquals("TCP - RTSP-Real Time Streaming Protocol(IP cameras and video Stream retrieval",ports.get(554));
        assertEquals("TCP - RDP-Remote Desktop Protocol(graphical remote control",ports.get(3389));

        assertEquals("TCP - MQTT(Message Queuing Telemetry Transport)",ports.get(1883));
        assertEquals("TCP - MQTTS(MQTT over SSL/TLS)encrypting IoT messaging",ports.get(8883));
        assertEquals("TCP - Chromecast(Google home local discovery and control services",ports.get(8008));
        assertEquals("TCP - AMQP(Advanced Message Queuing Protocol",ports.get(5672));
        assertEquals("TCP - Industrial Automation,PLCs,and smart Sensors",ports.get(502));
        assertEquals("TCP - DNP3(Distributed Network Protocol)",ports.get(20000));
    }
}
