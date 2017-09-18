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

            //Print code, for testing purposes TODO: delete this later
            System.out.println(code);
            
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
                    incomingFile(sc,fileName);
                default:
                    System.out.println("There was an error recieving file");
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
    
    /**
        Accepts incoming file
        @param
     */
    public static void incomingFile(SocketChannel s, String fileName){
        try {
            ByteBuffer fileBuff = ByteBuffer.allocate(134217728);

            byte[] byteArray = fileBuff.array();
            s.read(fileBuff);
            FileOutputStream fileout = null;
            try {
                fileout = new FileOutputStream(fileName);
            } catch (FileNotFoundException fnfe) {
                System.out.println("File not found exception");
            }
            BufferedOutputStream buffout = new BufferedOutputStream(fileout);

            InputStream is = s.socket().getInputStream();
            System.out.println("There was an error retrieving file");

            int read = 0;

            while ((read = is.read(byteArray)) != -1) {
                buffout.write(byteArray, 0, read);
            }
            buffout.flush();
            System.out.println("Success!");
        }catch(IOException ioe){
            System.out.println("There was an error retrieving the file");
        }
    }
}