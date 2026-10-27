package project1.IoTScanner;

import java.util.function.Function;

@FunctionalInterface
public interface DeviceReachability {

    boolean isReachAble(String IpAddress);
}
