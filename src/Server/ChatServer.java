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
    public static DefaultComboBoxModel<String> model;
    JTextField messageField;
    JComboBox toWho;
    static JLabel countUserLabel;


    /**
     *      Constructor
     */
    public ChatServer(){
        setSize(600, 450);

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
        doc = textPane.getStyledDocument();


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
        model = new DefaultComboBoxModel<String>();
        toWho = new JComboBox(model);
        toWho.setActionCommand("toWho");
        toWho.addActionListener(this);
        panel_1.add(sendTo, c);
        panel_1.add(toWho, c);


        c.gridy = 1;
        JLabel sendMessageLabel = new JLabel("Send Message:");
        sendMessageLabel.setHorizontalAlignment(JLabel.RIGHT);
        messageField = new JTextField();
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
        c.gridx = 0;
        countUserLabel = new JLabel("There are 0 users online");
        panel_1.add(countUserLabel, c);




    }

     /**
     *       Action Listener to listen button
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Port Config":
                PortConfig portConfig = new PortConfig();
                portConfig.show();
                break;

            // Remember to set the button on and off!
            case "Startup Service":
                server = new Server(textPane);
                server.setPortNo(portNo);
                server.start();
                stopServiceButton.setEnabled(true);
                startUpServiceButton.setEnabled(false);
                model.addElement("All Users");
                break;

            // Remember to set the button on and off!
            case "Stop Service":
                if(server == null){
                    System.out.println("You have't Start any Server!");
                }else{
                    server.stopServer();
                    stopServiceButton.setEnabled(false);
                    startUpServiceButton.setEnabled(true);
                    model.removeAllElements();
                }
                break;

            case "Exit":
                System.exit(0);
                break;

            // The Server can send to all or send to specific user, actually it put the message into an arraylist
            // User will get the message when they check for update
            case "Send":
                if(messageField.getText().equals("")){
                    break;      //do nothing if no message
                }else{
                    //Send to all Users
                    if(toWho.getSelectedItem().toString().equals("All Users")){
                        String content = "";
                        content += "Admin Say: ";
                        content += messageField.getText();
                        for(int i = 0; i < Server.clientDataArrayList.size(); i++){
                            // The user ID 9999 is not important since we don't use it this time
                            Message message1 = new Message(9999, Server.clientDataArrayList.get(i).id, content);
                            Server.messageArrayList.add(message1);
                        }
                        String show = content;

                        // Show on the textPane
                        try {
                            ChatServer.doc.insertString(0, show, null);
                        } catch (Exception f){
                            f.printStackTrace();
                        }

                    } else {
                        //Send to specific user
                        String content = "";
                        content += "Admin Say To ";
                        content += ( toWho.getSelectedItem().toString().split(",") )[0];
                        content += "(" + (toWho.getSelectedItem().toString().split(","))[1] + ") :";
                        content += messageField.getText();

                        int toID = Integer.parseInt( (toWho.getSelectedItem().toString().split(","))[1] );
                        Message message1 = new Message(9999,toID, content);
                        Server.messageArrayList.add(message1);
                    }
                }
                break;

            default:
                break;
        }

    }

    public static void main(String args[]) {
        ChatServer frame = new ChatServer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
    }

}
