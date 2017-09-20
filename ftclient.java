import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

class ftclient{
    public static void main(String args[]){
        try{
            SocketChannel sc = SocketChannel.open();
            Console cons = System.console();

            //obtain IP address from user and checks for validity
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
                //sc.connect(new InetSocketAddress(port));
            }catch(NumberFormatException nfe){
                System.out.println("Port must be a valid integer between 1024 and 65535. Closing program...");
                return;
            }
            
            //Connect to server
            sc.connect(new InetSocketAddress(ip,port));
            String fileName = cons.readLine("Enter the name of requested file: ");
            ByteBuffer buffer = ByteBuffer.wrap(fileName.getBytes());
            sc.write(buffer);

            //create new buffer and allocate space for return code
            ByteBuffer buff = ByteBuffer.allocate(65535);
            sc.read(buff);
            String code = new String(buff.array());
            code = code.trim();
            
            
            String message;
            switch(code){
                //Incoming error message
                case "0":
                    sc.read(buff);
                    message = new String(buff.array());
                    System.out.println(message);
                //Incoming message
                case "1":
                    sc.read(buff);
                    message = new String(buff.array());
                    System.out.println(message);
                //incoming file
                case "2":
                    //incomingFile(sc,fileName);
                    try {
                        
                        ByteBuffer fileBuff = ByteBuffer.allocate(60000000);
                        sc.read(fileBuff);
                        
                        byte[] byteArray = fileBuff.array();
                        System.out.println("waiting for data..");
                        
                        FileOutputStream fileout = null;
                        File f = new File(fileName.substring(1));

                        FileChannel fc = new FileOutputStream(f, false).getChannel();
                        fileBuff.flip();
                        fc.write(fileBuff);
                        fc.close();

                        System.out.println("Success!");

                    }catch(IOException e){
                        System.out.println("There was an error retrieving the file");
                    }
                
            }
            
        }catch(IOException e){
            System.out.println("Server Unreachable. Closing program..");
            return;
        }
    }

    /****
    * Checks validity of user given IP address
    * 
    * @param ip user typed IP address
    * @return true if valid, false if not
    ****/
    public static boolean validitycheck(String ip){
        try{
            String[] iparray = ip.split("\\.");
            int[] ipintarray = new int[iparray.length];
            for(int i = 0; i < iparray.length; i++){
                ipintarray[i] = Integer.parseInt(iparray[i]);
            }
            if(ipintarray.length != 4){
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