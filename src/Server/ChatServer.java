package Server;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Krauser on 26/4/2016.
 */
public class ChatServer extends JFrame implements ActionListener{

    public JTextPane textPane = new JTextPane();
    public static String portNo = "8888";
    public Server server;
    public static StyledDocument doc;

    JButton startUpServiceButton;
    JButton stopServiceButton;
    JButton notificationButton;
    static JLabel countUserLabel;
    /*
     *      Constructor
     */

    public ChatServer(){
        setSize(400, 300);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnConfiguration = new JMenu("Service(V)");
        menuBar.add(mnConfiguration);

        JMenu mnNewMenu = new JMenu("Help(H)");
        menuBar.add(mnNewMenu);



        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        JButton portConfigButton = new JButton("Port Config");
        panel.add(portConfigButton);
        portConfigButton.addActionListener(this);

        startUpServiceButton = new JButton("Startup Service");
        panel.add(startUpServiceButton);
        startUpServiceButton.addActionListener(this);

        stopServiceButton = new JButton("Stop Service");
        stopServiceButton.setEnabled(false);
        panel.add(stopServiceButton);
        stopServiceButton.addActionListener(this);

        JButton exitButton = new JButton("Exit");
        panel.add(exitButton);
        exitButton.addActionListener(this);

        /*
         *          Middle TextPanel
         */

        getContentPane().add(textPane, BorderLayout.CENTER);
        textPane.setEditable(false);
        textPane.setText( "original text" );
        doc = textPane.getStyledDocument();


//  Add some text

        try
        {
            doc.insertString(0, "Start of text\n", null );
            doc.insertString(doc.getLength(), "\nEnd of text", null );
        }
        catch(Exception e) { System.out.println(e); }



        /*
         *      Bottom GridBagLayout
         */
        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.SOUTH);


        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;


        panel_1.setLayout(g);

        c.gridy = 0;
        c.ipadx = 60;
        JLabel sendTo = new JLabel("Send To:");
        sendTo.setHorizontalAlignment(JLabel.RIGHT);
        JComboBox toWho = new JComboBox();
        panel_1.add(sendTo, c);
        panel_1.add(toWho, c);


        c.gridy = 1;
        JLabel sendMessageLabel = new JLabel("Send Message:");
        sendMessageLabel.setHorizontalAlignment(JLabel.RIGHT);
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        panel_1.add(sendMessageLabel, c);
        c.gridwidth = 3;
        c.ipadx = 300;
        panel_1.add(messageField, c);
        c.gridwidth = 1;
        c.ipadx = 0;
        panel_1.add(sendButton, c);


        c.gridy = 2;
        c.gridx = 3;
        c.gridwidth = 2;
        c.ipadx = 0;
        notificationButton = new JButton("Notify all users");
        notificationButton.addActionListener(this);
        panel_1.add(notificationButton, c);

        c.gridy = 2;
        c.gridx = 0;
        countUserLabel = new JLabel("There are 0 users online");
        panel_1.add(countUserLabel, c);




    }

     /*
     *  Action Listener to listen button
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Port Config":
                PortConfig portConfig = new PortConfig();
                portConfig.show();
                break;
            case "Startup Service":
                server = new Server(textPane);
                server.setPortNo(portNo);
                server.start();
                stopServiceButton.setEnabled(true);
                startUpServiceButton.setEnabled(false);
                break;
            case "Stop Service":
                if(server == null){
                    System.out.println("You have't Start any Server!");
                }else{
                    server.stopServer();
                    stopServiceButton.setEnabled(false);
                    startUpServiceButton.setEnabled(true);
                }
                break;
            case "Exit":
                System.out.println(portNo);
                //System.exit(0);
                break;
            case "Send":

                break;
            case "Notify all users":

            default:
                System.out.println("It should not happened");
                break;
        }

    }

    public static void main(String args[]) {
        ChatServer frame = new ChatServer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
    }

}
