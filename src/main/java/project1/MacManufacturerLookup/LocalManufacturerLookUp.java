package project1.MacManufacturerLookup;
@FunctionalInterface
public interface LocalManufacturerLookUp {

    String lookupManufacturer(String macAddress);
}
