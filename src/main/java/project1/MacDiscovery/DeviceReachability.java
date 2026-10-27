package project1.MacDiscovery;

import project1.IoTScanner.IoTScanner;

public class DeviceReachability {

    private final CommandRunner commandRunner;

    public DeviceReachability(){
        this(IoTScanner::runSystemCommand);
    }

    //Use to test the logic in test
    public DeviceReachability(CommandRunner commandRunner)
    {this.commandRunner = commandRunner;
    }
}
