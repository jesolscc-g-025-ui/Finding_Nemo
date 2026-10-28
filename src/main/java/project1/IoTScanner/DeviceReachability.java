package project1.IoTScanner;

import project1.MacDiscovery.CommandRunner;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class DeviceReachability {

    private final CommandRunner commandRunner;

    //Testing Logic purposes
    public DeviceReachability(CommandRunner commandRunner){
        this.commandRunner = commandRunner;
    }
    //Run System Command from the Actual System
    public DeviceReachability(){
        this(IoTScanner::RunSystemCommand);
    }

    public boolean isReachAble(String IpAddress)throws
            Exception {
        String Command = commandRunner.run(List.of("ping", "-n", "1", "-w", "1000"));
        Process process = new ProcessBuilder(Command, IpAddress).redirectErrorStream(true).start();

        int exitCode = process.waitFor();
        return exitCode == 0;
    }
}
