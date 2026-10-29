package project1.IoTScanner;

import project1.MacDiscovery.CommandRunner;
import project1.MacDiscovery.MacDiscovery;
import project1.MacManufacturerLookup.ApiManufacturerLookUp;
import project1.MacManufacturerLookup.LocalManufacturerLookUp;
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
    private final LocalManufacturerLookUp localLookup;
    private final ApiManufacturerLookUp apiLookup;

    //Constructor for actual tcpConnection,uses real ping.
    public IoTScanner(){
        this(IoTScanner::checkTcpConnection,new MacDiscovery()
                ,new DeviceReachability()::isReachAble,
                new MacManufacturerApi(),new MacManufacturerLookUp());

    }
    //Constructor for Testing Port Openings
    public IoTScanner(PortChecker portChecker){
        this(portChecker,new MacDiscovery(),new DeviceReachability()::isReachAble,
                new MacManufacturerApi(),new MacManufacturerLookUp());

    }
    //Constructor for testing injection with discovery and reachability
    public IoTScanner(PortChecker portChecker,MacDiscovery macDiscovery,ReachabilityCheck reachabilityCheck){
        this(portChecker,macDiscovery,reachabilityCheck,new MacManufacturerApi(),new MacManufacturerLookUp());

    }
    //Constructor for testing Manufacturer lookup and fallback
    public IoTScanner(PortChecker portChecker,
                      MacDiscovery macDiscovery,
                      ReachabilityCheck reachabilityCheck,
                      ApiManufacturerLookUp apiLookup,
                      LocalManufacturerLookUp localLookup){

        this.portChecker = portChecker;
        this.macDiscovery = macDiscovery;
        this.reachabilityCheck = reachabilityCheck;
        this.apiLookup = apiLookup;
        this.localLookup = localLookup;

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
            //Create a portChecker that Attempts an actual Network Connections
            //              |        |       |
            //              v        v       v
            IoTScanner scanner = new IoTScanner();
            List<PortScanResult> results = scanner.scan(ipAddress);

            ScanReport report = new ScanReport();
            String FullReport = report.generateReport(ipAddress, results);
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

        for (Map.Entry<Integer,String>enter: availablePorts().entrySet()) {

            int port = enter.getKey();
            String service = enter.getValue();

            if (isPortOpen(host, port)) {
                PortScanResult ScanResult = new PortScanResult(host, port, service, "Unknown", "Unknown manufacturer");
                results.add(ScanResult);
            }
        }
        if (results.isEmpty()){
            return results;
        }

        Optional<String>MacResult = macDiscovery.discover(host);
        String macAddress = MacResult.orElse("Unknown");
        String manufacturer =  "Unknown manufacturer";

        if (MacResult.isPresent()){
            manufacturer = findManufacturer(host,macAddress);
        }
        List<PortScanResult>UpdatedResult = new ArrayList<>();

        for (PortScanResult result : results){
            UpdatedResult.add(new PortScanResult(result.getHost(),result.getPort(),result.getService(),macAddress,manufacturer));
        }
        return UpdatedResult;
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

private  String findManufacturer(String Ip_Address,String macAddress)throws Exception{


        boolean isReachable = reachabilityCheck.isReachAble(Ip_Address);

    if (isReachable) {
            try {
                String manufacturer = apiLookup.lookupManufacturer(macAddress);

                if (manufacturer != null && !manufacturer.isEmpty() && !manufacturer.equalsIgnoreCase("unknown Manufacturer")) {
                    return manufacturer;
                }
            } catch (Exception e) {
                System.err.println("API unavailable,Using local CSV registry");

            }
        }
        return localLookup.lookupManufacturer(macAddress);
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

