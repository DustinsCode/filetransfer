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
                ByteBuffer buffer = ByteBuffer.allocate(4096);
                sc.read(buffer);
                String fileName = new String(buffer.array());
                if(fileName != null){
                    try{
                        //TODO: keeps throwing nullpointer
                        //note: make client and try filename= "/testfile.txt"
                        InputStream is = ftserver.class.getResourceAsStream(fileName);
                        try{
                            buffer = ByteBuffer.wrap(toByteArray(is));
                            sc.write(buffer);
                        }catch(IOException ioe){
                            String error = "There was an error converting the file";
                            buffer = ByteBuffer.wrap(error.getBytes());
                            sc.write(buffer);
                        }
                    }catch(NullPointerException npe){
                        //String error = "The file " + fileName + " does not exist.";
                        String error = "That file doesn't exist, bro.";
                        buffer = ByteBuffer.wrap(error.getBytes());
                        sc.write(buffer);
                    }
                    
                }
            }
        }catch(IOException e){
            System.out.println("Got an IO exception. Closing program...");
            return;
        }
    }

    /**
    * It would be way too convenient for Java
    * to include this method.
    *
    * @param InputStream of the file to send
    * @return byte[] array of bytes from the inputstream
    * */
    public static byte[] toByteArray(InputStream instream) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] be = new byte[2048];
        int offset = 0;
        while((offset = instream.read(be, 0, be.length)) != -1){
            os.write(be, 0, offset);
        }
        return os.toByteArray();


    }
}
