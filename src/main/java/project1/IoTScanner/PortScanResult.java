package project1.IoTScanner;

public class PortScanResult {
    private final String Host;
    private final Integer Port;
    private final String Service;
    private final String macAddress;
    private final String manufacturer;

    public PortScanResult(String host,int port,String Service){
        this(host,port,Service,null,"Unknown manufacturer");
    }
    public PortScanResult(String host,int port,String service,String macAddress,String manufacturer){
        this.Host = host;
        this.Port = port;
        this.Service = service;
        this.macAddress = macAddress;
        this.manufacturer = manufacturer;
    }

    public String getHost(){return Host;}
    public int getPort(){return Port;}
    public String getService(){return Service;}
    public String getMacAddress(){return macAddress;}
    public String getManufacturer(){return manufacturer;}
    @Override

    public String toString(){
        return "Host: " + getHost() +
                ", Port: " + getPort() +
                ", Service: " + getService() +
                ", macAddress: " + getMacAddress() +
                ", Manufacturer: " + getManufacturer();
    }
}
