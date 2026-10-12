package project1.netScanner;
import java.io.IOException;
import java.time.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class netScanner {
    private List<Integer> availablePorts;

    public netScanner(){

        this.availablePorts = new ArrayList<>();
    }
    public static void main(String[] args) {
        netScanner scanner = new netScanner();
        scanner.portScanner("localhost");
        System.out.println("Amount of available ports " + scanner.availablePorts().size());
    }

    public void portScanner(String host){
        // Firstly Set the target host

        // ports Range
        int startPort = 1;
        int endPort = 65535;

        //loop through port range
        for (int i = startPort; i <= endPort;i++){
            //try and connect to the port with a socket
            try{
                Socket socket = new Socket();
                String ip = socket.getInetAddress().getHostAddress();
                socket.connect(new InetSocketAddress(host,i),1000);
                System.out.println("Port " + i + " is open");
                availablePorts.add(i);
                socket.close();
            } catch (Exception e) {

            }
        }
    }

    public List<Integer> availablePorts(){
        return Collections.unmodifiableList(availablePorts);
    }
}
