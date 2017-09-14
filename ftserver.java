import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/*********************************
* TCP File Transfer Project
*
* @author Dustin Thurston
*********************************/

class ftserver{
    public static void main(String args[]){
        try{
            ServerSocketChannel c = ServerSocketChannel.open();
            Console cons = System.console();

            //Checks for valid port number
            try{
                int port = Integer.parseInt(cons.readLine("Enter port number: "));
                if(port < 1024 || port > 65535){
                    throw new NumberFormatException();
                }
                c.bind(new InetSocketAddress(port));
            }catch(NumberFormatException nfe){
                System.out.println("Port must be a valid integer between 1024 and 65535. Closing program...");
                return;
            }

            //Accept and handle connections
            while(true){
                SocketChannel sc = c.accept();
                if(sc != null){
                    System.out.println("A client has connected!");
                }
                
            }
        }catch(IOException e){
            System.out.println("Got an IO exception. Closing program...");
            return;
        }
    }
}