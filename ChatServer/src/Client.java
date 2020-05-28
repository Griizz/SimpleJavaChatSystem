import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{

    String username;
    ChatServer server;
    Socket socket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    private boolean connected, loggedIn;

    public Client(Socket client, ChatServer server)
    {
        this.server = server;
        socket = client;
        try
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected = true;
        }
        catch(IOException e)
        {
            try
            {
                socket.close();
            }
            catch (IOException e2)
            {
                socket = null;
            }
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                String line = in.readLine();
                if (!loggedIn)
                {
                    String ip = socket.getInetAddress().toString();
                    loggedIn = server.CheckLoginInfo(line, ip);
                    out.println(loggedIn);
                    if(loggedIn)
                    {
                        username = line.split("\\;")[0];
                        server.SendMessage(username + " ist dem Chat beigetreten.");
                    }

                }
                else
                {
                    server.Broadcast("<b>" + username + ":</b> " + line + "<br>");
                }
            }
            catch (IOException e)
            {
                server.SendMessage(username + " hat den Chat verlassen.");
                return;
            }

        }
    }

    public void SendMessage(String message)
    {
        if(connected )
            if(loggedIn)
                out.println(message);
        else
            server.RemoveClient(this);
    }
}
