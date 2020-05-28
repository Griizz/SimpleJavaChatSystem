import org.apache.commons.net.telnet.TelnetInputListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class ChatClient extends JFrame implements ActionListener, Runnable {

    SocketClient socket;

    JLabel userLabel, passwordLabel;
    JButton sendButton, loginButton;
    JPanel loginPanel, userPanel, passwordPanel, chatPanel, messagePanel;
    JTextField userField, messageField;
    JTextArea chatArea;
    JPasswordField passwordField;
    JScrollPane scrollPane;
    JEditorPane chatPane;

    public ChatClient()
    {
        socket = new SocketClient(this);
        ShowLoginWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == sendButton)
        {
            String msg = messageField.getText();
            messageField.setText("");
            if(msg != null && !msg.equals(""))
                socket.SendMessage(msg);
        }
        else if(source == loginButton)
        {
            String msg = userField.getText() + ";" + passwordField.getText();
            socket.SendMessage(msg);
            String response = socket.ReadMessage();
            if(response.equals("true"))
                ShowChatWindow();
            else
                passwordField.setText("");
        }
    }

    private void ShowLoginWindow()
    {
        userLabel = new JLabel("Username: ");
        userField = new JTextField(25);
        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(25);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setEnabled(socket.isConnected); //disable Login Button when no Server was found.

        userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(userLabel);
        userPanel.add(userField);

        passwordPanel = new JPanel();
        passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.add(userPanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(loginButton);
        loginPanel.setBorder(new TitledBorder(new EtchedBorder(), "Enter your Login Data"));

        getContentPane().add(loginPanel);

        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void ShowChatWindow()
    {
        chatArea = new JTextArea(25, 64);
        chatPane = new JEditorPane("text/html", "");
        chatPane.setEditable(false);
        chatPane.setText(chatArea.getText());
        scrollPane = new JScrollPane(chatPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        messageField = new JTextField( 80);
        sendButton = new JButton("Send");
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

        getContentPane().remove(loginPanel);
        getContentPane().add(chatPanel);

        this.setSize(1024, 768);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

       new Thread(this).start();
    }

    private void UpdateChat()
    {

    }


    @Override
    public void run()
    {
        while(true)
        {
                String msg = socket.ReadMessage();
                chatArea.append(msg);
                chatPane.setText(chatArea.getText());
        }
    }
}
