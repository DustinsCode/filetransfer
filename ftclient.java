import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class ftclient{
    public static void main(String args[]){
        try{
            SocketChannel sc = SocketChannel.open();
            Console cons = System.console();
            String ip = cons.readLine("Enter IP address: ");
            int port = Integer.parseInt(cons.readLine("Enter port number: "));
            sc.connect(new InetSocketAddress(ip,port));
            
        }catch(IOexception eP){
            System.out.println("Got an exception");
        }
    }

    private static boolean validitycheck(String ip, int port){
        if(port < 1024 || port > 65535)
    }
}