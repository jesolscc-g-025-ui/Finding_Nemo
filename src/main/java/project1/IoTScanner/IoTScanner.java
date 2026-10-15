package project1.IoTScanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class IoTScanner {

    private static final Map<Integer,String> ports = new LinkedHashMap<>();


    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Example: IotScanner <Device-ip>");
        return;
        }
        addPorts();
        scan(args[0]);
    }

    public static boolean isPortOpen(String host,int port){

        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host,port),500);
        } catch (IOException e) {
            return false;}
        return true;

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

    public static void scan(String host){

        System.out.println("SCANNING: " + host);
        System.out.println("--------------------------------");
        for (Map.Entry<Integer,String>enter: ports.entrySet()){
            int port = enter.getKey();
            String service = enter.getValue();
            if (isPortOpen(host,port)){
                System.out.println("Open Port: " + port + "- Service: " + service);
            }
            }
        System.out.println(".Scan Complete.");
        }
    }

