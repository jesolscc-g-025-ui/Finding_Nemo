package project1.MacManufacturerLookup;

import java.io.IOException;

@FunctionalInterface
public interface ApiManufacturerLookUp {
    String lookupManufacturer(String macAddress)throws IOException,InterruptedException;
}
