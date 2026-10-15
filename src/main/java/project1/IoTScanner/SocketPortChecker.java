package project1.IoTScanner;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketPortChecker implements PortChecker {

    @Override

    public boolean isOpen(String host,int port){
        try(Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host,port),500);
        } catch (IOException e) {
            return false;}
        return true;

    }
    }

