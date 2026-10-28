package project1.IoTScanner;

import java.io.IOException;

@FunctionalInterface
public interface ReachabilityCheck {
    boolean isReachAble(String ipAddress)throws Exception;
}
