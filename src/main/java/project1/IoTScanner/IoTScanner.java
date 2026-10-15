package project1.IoTScanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class IoTScanner {

    private static final Map<Integer,String> ports = new LinkedHashMap<>();
    private  PortChecker portChecker;

    public IoTScanner(PortChecker portChecker){
        this.portChecker = portChecker;
        addPorts();
    }

    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Example: IotScanner <Device-ip>");
        return;
        }

        //Create a portChecker that establishes actual Network Connections

        PortChecker portChecker1 = new SocketPortChecker();
        IoTScanner scanner = new IoTScanner(portChecker1);

        Map<Integer,String>results = scanner.scan(args[0]);

        System.out.println("Scanning: " + args[0]);
        System.out.println("-------------------------");

        results.forEach((port, service) ->
                System.out.println(
                        "OPEN: " + port + " - " + service
                )
        );

        System.out.println(
                "Total open ports: " + results.size()
        );

    }


    public static void addPorts() {
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

    public Map<Integer,String> scan(String host){

        Map<Integer,String>openPorts = new LinkedHashMap<>();

        for (Map.Entry<Integer,String>enter: availablePorts().entrySet()){
            int port = enter.getKey();
            String service = enter.getValue();
            if (portChecker.isOpen(host,port)){
                openPorts.put(port,service);
            }
            }
        return Collections.unmodifiableMap(openPorts);
    }
    }

