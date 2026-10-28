package project1.MacDiscovery;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TestMACDiscovery {
    private static final String Test_IP = "192.168.5.64";
    @Test
    void TestARPOutputMacAddressFindSuccessfully(){

        String arpOutput = "Interface: 172.29.0.1 --- 0x36\n" +
                "  Internet Address      Physical Address      Type\n" +
                "  172.29.10.55          00-15-5d-04-17-64     dynamic"
                ;

        Optional<String>result = MacDiscovery.parseMACAddress(arpOutput);

        assertEquals(Optional.of( "00:15:5D:04:17:64"),result);
    }
    //TestShouldDiscoverMACAddress with refactored Runner Interface to Simulate Ping Condition
    @Test

    void TESTARPOutputLettersShouldBeConvertedToUpperCase(){
        String output ="Interface: 172.29.0.1 --- 0x36\\n\" +\n" +
                "                \"  Internet Address      Physical Address      Type\\n\" +\n" +
                "                \"  172.29.10.55          ff-ff-ff-ff-ff-ff     dynamic";

        Optional<String>result = MacDiscovery.parseMACAddress(output);

        assertEquals(Optional.of("FF:FF:FF:FF:FF:FF"),result);
    }
    @Test
    void TestReturnEmptyIfNoMACAddressIsNotFound(){
        String ARPOutput ="Interface: 172.29.0.1 --- 0x36\\n\" +\n" +
                "                \"  Internet Address     \n" +
                "                \"  172.29.10.55         ";

        Optional<String>result = MacDiscovery.parseMACAddress(ARPOutput);

        assertEquals(Optional.empty(),result);
    }
    @Test
    void TestReturnEmptyForEmptyOutput(){
        Optional<String>result = MacDiscovery.parseMACAddress("");

        assertEquals(Optional.empty(),result);
    }
    @Test
    void TestReturnEmptyForNullOutput(){
        Optional<String>result= MacDiscovery.parseMACAddress(null);

        assertEquals(Optional.empty(),result);
    }

    //TestShouldDiscoverMACAddress with refactored Runner Interface to Simulate Ping Condition

    @Test

    void shouldReturnMACAddress()throws Exception {

        CommandRunner TestRunner = command ->{
            //Check if its the ping command
            if (command.getFirst().equals("ping")){
                return "Ping Completed";
            }

            //Check if its the ARP command
            if (command.getFirst().equals("arp")){
                return Test_IP + " aa-bb-cc-dd-ee-ff dynamic";
            }
            throw new IOException("Unexpected Command");
        };
        MacDiscovery discovery = new MacDiscovery(TestRunner);

        Optional<String>result = discovery.discover(Test_IP);

        assertEquals(Optional.of("AA:BB:CC:DD:EE:FF"),result);
    }

    //Test Should Run Ping Before ARP

    @Test
    void ShouldRunTestBeforeArp()throws Exception{
        //Atomic count is used for Multithread Actions of incrementing
        AtomicInteger callCount = new AtomicInteger();

        CommandRunner TestRunner = command -> {
            int call = callCount.getAndIncrement();

            //Check to see if ping Command is run first
            if (call == 0){
                assertEquals(List.of("ping","-n","1","-w","1000",Test_IP),command);
                return "Ping Completed";
            }
            //then ARP command
            assertEquals(List.of("arp","-a",Test_IP),command);
                return Test_IP + " aa:bb:cc:dd:ee:ff" + " dynamic";
        };
        MacDiscovery discovery = new MacDiscovery(TestRunner);

         discovery.discover(Test_IP);
        //Check the ARP counter
        assertEquals(2,callCount.get());
    }

    @Test

    void ShouldReturnEmptyWhenARPhasNoMACAddress()throws Exception{

        CommandRunner TestRunner = command -> command.get(0).equals("ping")
                ? "Ping completed" : "no matching ARP entry";

        MacDiscovery discovery = new MacDiscovery(TestRunner);
        Optional<String >result = discovery.discover(Test_IP);



        assertTrue(result.isEmpty());
    }
    @Test

    void TestShouldPropagateCommandFailure(){

        CommandRunner TestRunner = command -> {
            throw new IOException("Unsupported Command");
        };

        MacDiscovery discovery = new MacDiscovery(TestRunner);
        assertThrows(IOException.class,()->discovery.discover(Test_IP));
    }

    @Test

    void TestShouldReturnEmptyWhenARPisEmpty()throws Exception{

        CommandRunner TestRunner = command -> "";

        MacDiscovery discovery = new MacDiscovery(TestRunner);
        Optional<String>result = discovery.discover(Test_IP);

        assertTrue(result.isEmpty());
    }

    @Test
    void ShouldStillRunARPWithoutPingSucceeding()throws Exception{

        AtomicInteger count = new AtomicInteger();

        CommandRunner TestRunner = command -> {
            count.incrementAndGet();

            if(command.getFirst().equals("ping")){
                throw new IOException("Ping command Failed");
            }

            if (command.getFirst().equals("arp")){
                return Test_IP + " aa-bb-cc-dd-ee-ff dynamic";
            }

            throw new IOException("unsupported Command");
        };

        MacDiscovery discovery = new MacDiscovery(TestRunner);
        Optional<String>result = discovery.discover(Test_IP);

        assertEquals(2,count.get());

        assertEquals(Optional.of("AA:BB:CC:DD:EE:FF"),result);
    }

}
