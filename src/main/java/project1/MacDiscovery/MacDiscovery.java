package project1.MacDiscovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacDiscovery {

    //The pattern to match for MACAddress
    private static final Pattern pattern = Pattern.compile("(?i)([0-9a-f]{2}-){5}[0-9a-f]{2}");

    private final CommandRunner commandRunner;

    //Used For Running Actual System Commands
    public MacDiscovery(){
        this(MacDiscovery::runSystemCommand);
    }
    //Used by Unit Tests
    public MacDiscovery(CommandRunner commandRunner){
        this.commandRunner = commandRunner;
    }
    public Optional<String>discover(String ipAddress) throws Exception,InterruptedException {


        //First try to load the ARP cache with the actual ping,which means the target device accepts ping
        try {
            commandRunner.run(List.of("ping", "-n", "1", "-w", "1000", ipAddress));
        } catch (IOException e) {
        //continue even if ping failed for ping-disabled devices
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
        //Then query the ARP cache for the target Ip Address
        String ARPOutput = commandRunner.run(List.of("arp","-a",ipAddress));

        return parseMACAddress(ARPOutput);
    }

    //Method For Parsing Logic Without running windows operating Commands
    static Optional<String>parseMACAddress(String arpOutput){

        if (arpOutput == null || arpOutput.isBlank()){
            return Optional.empty();
        }


        Matcher match = pattern.matcher(arpOutput);

        if (match.find()){
            String macAddress = match.group(0).replace("-",":").toUpperCase(Locale.ROOT);
            return Optional.of(macAddress);
        }
        return Optional.empty();
    }
    private static String runSystemCommand(List<String>command)throws IOException,InterruptedException{

        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        try(Scanner scanner = new Scanner(process.getInputStream(),StandardCharsets.UTF_8)){

            scanner.useDelimiter("\\A");
            String output = scanner.hasNext() ?scanner.next() :"";

            int exitCode= process.waitFor();
            if (exitCode != 0){
                throw new IOException("Command Failed With Exit Code" + exitCode + " : " + command);
            }
            return output;
        }
        }
    }

