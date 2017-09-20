import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.Path.*;
import java.nio.file.*;

/*********************************
* TCP File Transfer Project
*
* @author Dustin Thurston
*********************************/

class ftserver{

    public final static String errorcode = "0";
    public final static String messagecode = "1";
    public final static String filecode = "2";

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
                ByteBuffer buffer = ByteBuffer.allocate(32768);
                sc.read(buffer);
                String fileName = new String(buffer.array());
                fileName = fileName.trim();
                if(fileName != null){
                    try{
                        System.out.println("Client trying to recieve " + fileName);
                        
                        
                        //InputStream is = ftserver.class.getResourceAsStream(fileName);
                        try{

                        //testing
                        Path filelocation = null;
                        try{
                            filelocation = Paths.get(ftserver.class.getResource(fileName).toURI());
                        }catch(URISyntaxException u){
                            //bullshit
                        }
                        byte[] data = Files.readAllBytes(filelocation);

                        //testing


                            buffer = ByteBuffer.wrap(filecode.getBytes());
                            sc.write(buffer);
                            
                            buffer = ByteBuffer.wrap(data);
                            //buffer = ByteBuffer.wrap(toByteArray(is));
                            sc.write(buffer);
                            System.out.println("The file has been sent.");
                            
                        }catch(IOException ioe){
                            String error = "There was an error converting the file";

                            //tells client an error message is coming
                            buffer = ByteBuffer.wrap(errorcode.getBytes());
                            sc.write(buffer);
                            //sends message
                            buffer = ByteBuffer.wrap(error.getBytes());
                            sc.write(buffer);
                        }
                    }catch(NullPointerException npe){
                        String error = "The file " + fileName + " does not exist.";
                        System.out.println("The client's file doesn't exist.");
                        buffer = ByteBuffer.wrap(errorcode.getBytes());
                        sc.write(buffer);
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
        os.flush();
        os.close();
        return os.toByteArray();
    }
}
