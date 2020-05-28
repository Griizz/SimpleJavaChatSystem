import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {

    public static void main(String[] args) //Launch Application
    {
         ChatServer server = new ChatServer();
        server.setTitle("Server");
        WindowListener l = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };

        server.addWindowListener(l);
        server.Start();
    }
}
