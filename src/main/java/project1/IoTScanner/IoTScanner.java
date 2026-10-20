package project1.IoTScanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class IoTScanner {

    private static final Map<Integer,String> ports = new LinkedHashMap<>();
    private final PortChecker portChecker;
    //Constructor for actual tcpConnection
    public IoTScanner(){
        this.portChecker = IoTScanner::checkTcpConnection;
        addPorts();
    }
    public IoTScanner(PortChecker portChecker){
        this.portChecker = portChecker;
        addPorts();
    }
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Example: IotScanner <Device-ip>");
        return;
        }

        //Create a portChecker that Attempts an actual Network Connections
        //              |        |       |
        //              v        v       v
        IoTScanner scanner = new IoTScanner();
        List<PortScanResult>results = scanner.scan(args[0]);

        ScanReport report = new ScanReport();
        String FullReport = report.generateReport(args[0],results);
        System.out.println(FullReport);
    }


    public void addPorts() {
        // general TCP ports you would commonly find on IoT devices

        ports.put(20,"TCP - FTP-Data(File transfer protocol)");

        // Web And Remote Management Ports
        ports.put(21,"TCP - FTP-Control");
        ports.put(22,"TCP - SSH-Secure Shell (Remote login / CLI)");
        ports.put(23,"TCP - Telnet-unencrypted remote terminal connection");
        ports.put(80,"TCP - HTTP-Unencrypted Web Traffic");
        ports.put(443,"TCP - HTTPS-Secure Web Management");
        ports.put(554,"TCP - RTSP-Real Time Streaming Protocol(IP cameras and video Stream retrieval");
        ports.put(3389,"TCP - RDP-Remote Desktop Protocol(graphical remote control");

        // Messaging and Application Protocols
        ports.put(1883,"TCP - MQTT(Message Queuing Telemetry Transport)");
        ports.put(8883,"TCP - MQTTS(MQTT over SSL/TLS)encrypting IoT messaging");
        ports.put(8008,"TCP - Chromecast(Google home local discovery and control services");
        ports.put(5672,"TCP - AMQP(Advanced Message Queuing Protocol");

        //Industrial And Field Protocols

        ports.put(502,"TCP - Industrial Automation,PLCs,and smart Sensors");
        ports.put(20000,"TCP - DNP3(Distributed Network Protocol)");
    }
    public Map<Integer,String> availablePorts(){
        return Collections.unmodifiableMap(ports);
    }

    public List<PortScanResult>scan(String host){

        List<PortScanResult>results = new ArrayList<>();


        for (Map.Entry<Integer,String>enter: availablePorts().entrySet()){

            int port = enter.getKey();
            String service = enter.getValue();

            if (isPortOpen(host,port)){
                PortScanResult ScanResult = new PortScanResult(host,port,service);
                results.add(ScanResult);
                System.out.println(ScanResult);
            }
            }

        return results;
    }
    private static boolean checkTcpConnection(String host,int port){
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host,port),500);
        }catch (IOException e){
            return false;
        }return true;
    }
public boolean isPortOpen(String host,int port){
        return portChecker.isOpen(host, port);
}
    }

