import org.apache.commons.net.telnet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SocketClient {

    Boolean isConnected;
    TelnetClient client;            //Creates the Socket Connection
    PrintWriter out = null;         //used to send Messages
    BufferedReader in = null;       //used to read messages

    /**
     * Constructor for the Class
     */
    public SocketClient(ChatClient chat)
    {
        client = new TelnetClient();
        isConnected = true;
        try
        {
            client.connect("localhost", 4444);
        } catch(IOException e)
        {
            isConnected = false;
        }
        if(isConnected)
        {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
    }

    public void SendMessage(String msg)
    {
        out.println(msg);
    }

    public String ReadMessage()
    {
        try{
            String line = in.readLine();
            return line;
        }
        catch (IOException e)
        {
            return null;
        }
    }

}
