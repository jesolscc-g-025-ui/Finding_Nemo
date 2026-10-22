package project1.MacDiscovery;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMACDiscovery {

    @Test
    void TestARPOutputMacAddressFindSuccessfully(){

        String arpOutput = "Interface: 172.29.0.1 --- 0x36\n" +
                "  Internet Address      Physical Address      Type\n" +
                "  172.29.10.55          00-15-5d-04-17-64     dynamic"
                ;

        Optional<String>result = MacDiscovery.parseMACAddress(arpOutput);

        assertEquals(Optional.of( "00:15:5D:04:17:64"),result);
    }
    @Test

    void TESTARPOutputLettersShouldBeConvertedToUpperCase(){
        String output ="Interface: 172.29.0.1 --- 0x36\\n\" +\n" +
                "                \"  Internet Address      Physical Address      Type\\n\" +\n" +
                "                \"  172.29.10.55          ff-ff-ff-ff-ff-ff     dynamic";

        Optional<String>result = MacDiscovery.parseMACAddress(output);

        assertEquals(Optional.of("FF:FF:FF:FF:FF:FF"),result);
    }

}
