package project1.IoTScanner;

public interface PortChecker {
    //For unitTesting Purposes, To test the port Scanning logic without making real-tcp connections

    boolean isOpen(String host,int port);
}
