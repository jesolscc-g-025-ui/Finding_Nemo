package project1.IoTScanner;

import project1.MacDiscovery.CommandRunner;
import project1.MacDiscovery.MacDiscovery;
import project1.MacManufacturerLookup.MacManufacturerApi;
import project1.MacManufacturerLookup.MacManufacturerLookUp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IoTScanner {

    private static final Map<Integer,String> ports = new LinkedHashMap<>();

    private final PortChecker portChecker;
    private final MacDiscovery macDiscovery;
    private final ReachabilityCheck reachabilityCheck;

    //Constructor for actual tcpConnection
    public IoTScanner(){
        this(IoTScanner::checkTcpConnection,new MacDiscovery(),new DeviceReachability()::isReachAble);
        addPorts();
    }
    //Constructor for Testing Port Openings
    public IoTScanner(PortChecker portChecker){
        this(portChecker,new MacDiscovery(),new DeviceReachability()::isReachAble);
        addPorts();
    }
    public IoTScanner(PortChecker portChecker,MacDiscovery macDiscovery,ReachabilityCheck reachabilityCheck){
        this.portChecker = portChecker;
        this.macDiscovery = macDiscovery;
        this.reachabilityCheck = reachabilityCheck;

        addPorts();
    }

    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Example: IotScanner <Device-ip>");
        return;
        }
        String ipAddress = args[0];

        try {
            // First Discover the Mac Address of IpAddress
            MacDiscovery discovery = new MacDiscovery();
            Optional<String>mac = discovery.discover(ipAddress);
            String macAddress = mac.get();

            //Secondly Check if the device is online and try to use the API lookup
            String manufacturer = IoTScanner.findManufacturer(ipAddress,macAddress);

            //Device Information
            System.out.println("======== Device Info =====\n");
            System.out.println("IP: " + ipAddress+"\n");
            System.out.println("MAC: "+ mac.orElse("mac not found")+"\n");
            System.out.println("Manufacturer: " + manufacturer);
            System.out.println("--------------------------------");

            //Create a portChecker that Attempts an actual Network Connections
            //              |        |       |
            //              v        v       v
            IoTScanner scanner = new IoTScanner();
            List<PortScanResult> results = scanner.scan(args[0]);

            ScanReport report = new ScanReport();
            String FullReport = report.generateReport(args[0], results);
            System.out.println(FullReport);

        } catch (IOException e) {
            System.err.println("I/O Error " + e.getMessage());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("MAC Discovery Was Interrupted");

        } catch (Exception e) {
            throw new RuntimeException("unsupported Command From Mac Discovery");
        }
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

    public List<PortScanResult>scan(String host)throws Exception{

        List<PortScanResult>results = new ArrayList<>();

        MacDiscovery discovery = new MacDiscovery();
        Optional<String>macResult = discovery.discover(host);

        String MacAddress = macResult.orElse("Unknown");
        String manufacturer = macResult.isPresent() ? findManufacturer(host,MacAddress): "Unknown manufacturer";

        for (Map.Entry<Integer,String>enter: availablePorts().entrySet()){

            int port = enter.getKey();
            String service = enter.getValue();

            if (isPortOpen(host,port)){
                PortScanResult ScanResult = new PortScanResult(host,port,service,MacAddress,manufacturer);
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

private static String findManufacturer(String Ip_Address,String macAddress)throws Exception{

        MacManufacturerApi api = new MacManufacturerApi();
        MacManufacturerLookUp lookup = new MacManufacturerLookUp();

        boolean isReachable = new DeviceReachability().isReachAble(Ip_Address);

    if (isReachable) {
            try {
                String manufacturer = api.lookupManufacturer(macAddress);

                if (manufacturer != null && !manufacturer.isEmpty() && !manufacturer.equalsIgnoreCase("unknown Manufacturer")) {
                    return manufacturer;
                }
            } catch (Exception e) {
                System.err.println("API unavailable,Using local CSV registry");

            }
        }
        return lookup.lookupManufacturer(macAddress);
}
    public static String RunSystemCommand(List<String>command)throws IOException,InterruptedException{
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        try(Scanner scanner = new Scanner(process.getInputStream(), StandardCharsets.UTF_8)){

            scanner.useDelimiter("\\A");
            String output = scanner.hasNext() ?scanner.next() :"";

            int exitCode = process.waitFor();
            if (exitCode != 0){
                throw new IOException("Command Failed With Exit Code" + exitCode + " : " + command);
            }
            return output;
        }
    }

    }

