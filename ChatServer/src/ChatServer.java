import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer extends JFrame implements ActionListener
{

    JButton sendButton;
    JPanel chatPanel, messagePanel;
    JTextField messageField;
    JTextArea chatArea;
    JScrollPane scrollPane;
    JEditorPane chatPane;

    ServerSocket socket = null;
    List<Client> clients;
    UserDataBank db;



    public ChatServer()
    {
        Show();
        try{
            socket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Could not listen on port 4444");
            System.exit(-1);
        }
        clients = new ArrayList<>();
        db = new UserDataBank();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == sendButton)
        {
            String text = messageField.getText();
            messageField.setText("");
           SendMessage(text);
        }
    }

    public void Broadcast(String message)
    {
        for (Client c : clients) {
            c.SendMessage(message);
        }

        UpdateChat(message);
    }

    public void SendMessage(String message)
    {
        String msg = "<b>Server:</b> " + message + "<br>";
        Broadcast(msg);
    }

    public boolean CheckLoginInfo(String data, String ip)
    {
        return db.CheckLoginInfo(data, ip);
    }

    public void RemoveClient(Client client)
    {
        if(clients.contains(client))
            clients.remove(client);
    }

    public void Start()
    {
        while(true)
        {
            try {
                Socket s = socket.accept();
                Client c = new Client(s, this);
                clients.add(c);
                new Thread(c).start();
            } catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }
        }
    }

    private void UpdateChat(String message)
    {
        chatArea.append(message);
        chatPane.setText(chatArea.getText());
    }

    private void Show()
    {
        chatArea = new JTextArea(25, 64);
        chatPane = new JEditorPane("text/html", "");
        chatPane.setEditable(false);
        chatPane.setText(chatArea.getText());
        scrollPane = new JScrollPane(chatPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageField = new JTextField( 76);
        sendButton = new JButton("Broadcast");
        sendButton.addActionListener(this);

        messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        messagePanel.setAlignmentY(BOTTOM_ALIGNMENT);
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        messagePanel.setBorder(new TitledBorder(new EtchedBorder(), "Send Message"));

        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(BorderLayout.CENTER ,scrollPane);
        chatPanel.add(BorderLayout.PAGE_END, messagePanel);
        chatPanel.setBorder(new TitledBorder(new EtchedBorder(), "Chat"));

        getContentPane().add(chatPanel);

        this.setSize(1024, 768);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }


}
