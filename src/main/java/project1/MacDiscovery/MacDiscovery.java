package project1.MacDiscovery;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacDiscovery {

    private static final Pattern MacAddressPat= Pattern.compile("(?i)([0-9A-F]{2}-){5}[0-9A-F]{2}");

    public Optional<String>discover(String ipAddress)throws IOException,InterruptedException{

        //First We gonna ping the IpAddress with a ProcessBuilder
        Process ping = new ProcessBuilder("ping","-n","1","-w","1000",ipAddress).redirectErrorStream(true).start();

        try{
            ping.waitFor(); // wait for the ping process to finish
        }finally {
            ping.destroy(); // terminate pinging process
        }
        // Windows Specific Command
        Process arP = new ProcessBuilder("arp","-a", ipAddress).redirectErrorStream(true).start();

        try{
            String output; // the output from the arp

            try(Scanner scanner = new Scanner(arP.getInputStream())){
                output = scanner.useDelimiter("\\A").hasNext()? scanner.next() : "";
                //takes the output and use it as input for java to read
                //Convert the InputStream data to String output in Java through the Scanner
            }
            //Using the Data,Search for MacAddress
            return parseMACAddress(output);

        }finally {
            arP.destroyForcibly();
        }
    }

    //Method For Parsing Logic Without running windows operating Commands
    static Optional<String>parseMACAddress(String arpOutput){

        if (arpOutput == null || arpOutput.isBlank()){
            return Optional.empty();
        }
        //The pattern to match for MACAddress
        Pattern pattern = Pattern.compile("(?i)([0-9a-f]{2}-){5}[0-9a-f]{2}");

        Matcher match = pattern.matcher(arpOutput);

        if (match.find()){
            String macAddress = match.group(0).replace("-",":").toUpperCase(Locale.ROOT);
            return Optional.of(macAddress);
        }
        return Optional.empty();
    }
}
