package project1.IoTScanner;

public class PortScanResult {
    private final String Host;
    private final Integer Port;
    private final String Service;

    public PortScanResult(String host,int port,String Service){
        this.Host = host;
        this.Port = port;
        this.Service = Service;
    }

    public String getHost(){return Host;}
    public int getPort(){return Port;}
    public String getService(){return Service;}

    @Override

    public String toString(){
        return "Host: " + getHost() +
                ", Port: " + getPort() +
                ", Service: " + getService();
    }
}
