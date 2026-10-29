package project1.SecurityReport;

import project1.IoTScanner.PortScanResult;

import java.time.LocalDateTime;
import java.util.List;


public class ScanReport {

    private DeviceInfo device;
    private String target;
    private String scanDate;
    private ScanSummary summary;
    private List<PortScanResult> ports;

    public ScanReport(String target,String macAddress,String manufacturer,int totalPortsChecked,List<PortScanResult>ports){

        this.target = target;
        this.device = new DeviceInfo(
                target,macAddress,manufacturer
        );
        this.scanDate = LocalDateTime.now().toString();
        this.summary = new ScanSummary(
                totalPortsChecked,ports.size(),"Completed");
        this.ports = ports;

    }
    public static class DeviceInfo{
            private String ipAddress;
            private String macAddress;
            private String manufacturer;

            public DeviceInfo(String ipAddress,String macAddress,String manufacturer){
                this.ipAddress = ipAddress;
                this.macAddress = macAddress;
                this.manufacturer = manufacturer;
        }
    }
    public static class ScanSummary{
        private int totalPortsChecked;
        private int  openPorts;
        private String ScanStatus;

        private ScanSummary(int totalPortsChecked,int openPorts,String ScanStatus){
            this.totalPortsChecked = totalPortsChecked;
            this.openPorts = openPorts;
            this.ScanStatus = ScanStatus;
        }
    }
}
