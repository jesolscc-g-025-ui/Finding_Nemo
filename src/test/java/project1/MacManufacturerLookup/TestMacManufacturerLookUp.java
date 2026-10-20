package project1.MacManufacturerLookup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestMacManufacturerLookUp {

    private MacManufacturerLookUp lookUp;

    @BeforeEach

    void setUp()throws IOException{
        lookUp = new MacManufacturerLookUp();
        lookUp.loadRegistry();
    }
    @Test

    void TestLoadRegistryAndFindMALManufacturer(){
        String result = lookUp.lookupManufacturer("00:11:22:AA:BB:CC");

        assertEquals("Test Manufacturer Large",result);
    }

    @Test
    void TestLoadRegistryAndFindMAMManufacturer(){
        String result = lookUp.lookupManufacturer("AA:BB:CC:D0:E1:F2");

        assertEquals("Test Manufacturer Medium",result);
    }
    @Test
    void TestLoadRegistryAndFindMASManufacturer(){
        String result = lookUp.lookupManufacturer("12:34:56:78:9A:BC");

        assertEquals("Test Manufacturer Small",result);
    }
    @Test

    void TestMACAddressWithHyphens(){
        String result = lookUp.lookupManufacturer("12-34-56-78-9A-BC");

        assertEquals("Test Manufacturer Small",result);
    }
    @Test

    void TestMacAddressWithDots(){
        String result = lookUp.lookupManufacturer("12.34.56.78.9A.BC");

        assertEquals("Test Manufacturer Small", result);
    }

    @Test

    void TestMacAddressLowerCase(){
        String result = lookUp.lookupManufacturer("aa.bb.cc.d1.23.45");

        assertEquals("Test Manufacturer Medium",result);
    }

    @Test

    void TestUnknownManufacturer(){
        String result = lookUp.lookupManufacturer("FE:DC:BA:98:76:54");

        assertEquals("Unknown manufacturer",result);
    }
    @Test

    void TestExceptionForInvalidMACAddress(){
        assertThrows(IllegalArgumentException.class,()-> lookUp.lookupManufacturer("00.11.22"));
    }

    @Test
    void TestRejectMACWithInvalidCharacter(){
        assertThrows(IllegalArgumentException.class,()-> lookUp.lookupManufacturer("GG.AA.11.55.99.CC"));
    }

    @Test
    void TestRejectNullMACAddress(){
        assertThrows(NullPointerException.class,()-> lookUp.lookupManufacturer(null));
    }
    @Test
    void TestMostSpecificMatchingPrefix(){
        //Should test 9 before 7 and  6
        String result = lookUp.lookupManufacturer("12.34.56.78.9A.BC");

        assertEquals("Test Manufacturer Small",result);
    }
}
