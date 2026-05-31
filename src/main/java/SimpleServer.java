import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;
public class SimpleServer
{
    private ServerSocket server;
    private DataInputStream in1;
    public static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "3000"));
    public static final String STOP_STRING = "##";
    //ArrayList<Integer> rooms;
    private DataOutputStream out1;
    private DataInputStream in2;
    private DataOutputStream out2;
    private Socket socket1;
    private Socket socket2;

    public SimpleServer()
    {   
        try
        {
            server=new ServerSocket(PORT);
            initializeConnections();
            //rooms = new ArrayList<Integer>();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initializeConnections() throws IOException
    {
        socket1=server.accept();
        in1=new DataInputStream(new BufferedInputStream(socket1.getInputStream()));
        out1 =new DataOutputStream(socket1.getOutputStream());
        socket2=server.accept();
        in2=new DataInputStream(new BufferedInputStream(socket2.getInputStream()));
        out2 =new DataOutputStream(socket2.getOutputStream());
        readMessages();
        close();
    }

    private void close() throws IOException
    {
        try
        {
            in1.close();
            out1.close();
            in2.close();
            out2.close();
            server.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException
    {
        /*String line = "";
        while (!line.equals(STOP_STRING)) {
            line=in.readUTF();
            //System.out.println("refceived line: " + line);
            int start=line.indexOf(":");
            int stop=line.lastIndexOf(":");
            System.err.println("start=" + start + " stop=" + stop);
            String command = line.substring(0, start);
            System.out.println("the command is: "+command);
            if (command.equals("join")) {
                rooms.add(Integer.parseInt(line.substring(start+1,stop)));
            }
            else {
                System.out.println(line);
                out.writeUTF(line);// reprinting the line because the server doesn't need it
            }
            System.out.println(line);
        }*/
       String line1 = "";
       String line2 = "";
       while (!line1.equals(STOP_STRING) && !line2.equals(STOP_STRING))
       {
            out1.writeUTF("1");
            out2.writeUTF("2");
            line1=in1.readUTF(); //check white move
            System.out.println("refceived line 1: " + line1);
            out2.writeUTF(line1);

            line2=in2.readUTF(); //check black lost
            System.out.println("Line 2 is " + line2);
            if (line2.substring(6).equals("wins"))
            {
                System.out.println("Line gone 2");
                System.out.println("Line now 2 "+ line2);
                out1.writeUTF("winner1");
                out2.writeUTF("winner1");
            
                close(); //This makes it so you don't have to reboot the server to play again
                server=new ServerSocket(PORT);
                initializeConnections();
            }
            
            line2 = in2.readUTF();//black move
            System.out.println("refceived line 2: " + line2);
            out1.writeUTF(line2);


            line1 = in1.readUTF(); //check white lost
            if (line1.substring(6).equals("wins"))
            {
                System.out.println("Line gone 1");
                System.out.println("Line now 1 "+ line1);
                out1.writeUTF("winner2");
                out2.writeUTF("winner2");

                close();
                server=new ServerSocket(PORT);
                initializeConnections();
            }
       }
    }

    

    public static void main(String[] args)
    {
        new SimpleServer();
    }
}