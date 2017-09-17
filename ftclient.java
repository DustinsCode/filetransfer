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
            if(!validitycheck(ip)){
                return;
            }
            //Checks for valid port number
            int port = 0;
            try{
                port = Integer.parseInt(cons.readLine("Enter port number: "));
                if(port < 1024 || port > 65535){
                    throw new NumberFormatException();
                }
                sc.bind(new InetSocketAddress(port));
            }catch(NumberFormatException nfe){
                System.out.println("Port must be a valid integer between 1024 and 65535. Closing program...");
                return;
            }
             
            sc.connect(new InetSocketAddress(ip,port));
            
        }catch(IOException eP){
            System.out.println("Got an exception");
        }
    }

    public static boolean validitycheck(String ip){
        try{
            String[] iparray = ip.split("\\.");
            int[] ipintarray = new int[iparray.length];
            for(int i = 0; i < iparray.length; i++){
                ipintarray[i] = Integer.parseInt(iparray[i]);
            }
            if(ipintarray.length != 4){
                System.out.println("" + ipintarray.length);
                System.out.println("" + iparray.length);
                throw new NumberFormatException();
            }else{
                return true;
            }
        }catch(NumberFormatException nfe){
            System.out.println("Invalid IP address.  Closing program..");
            return false;
        }
    }
}