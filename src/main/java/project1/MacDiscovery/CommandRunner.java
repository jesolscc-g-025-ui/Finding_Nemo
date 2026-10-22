package project1.MacDiscovery;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface CommandRunner {

    String run(List<String>Command) throws Exception;
}
