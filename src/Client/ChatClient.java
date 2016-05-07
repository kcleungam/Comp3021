package Client; /**
 * Created by Krauser on 26/4/2016.
 */
import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ChatClient extends JFrame implements ActionListener{
    public static int id = -1;
    public static String username = "hkuster";
    public static String ipAddress = "127.1.1.0";
    public static int portNo = 8888;
    public Socket socket;
    public static StyledDocument doc;

    public static JTextField showUserID = new JTextField("Your name is:" + username);
    JButton loginBtn;
    JButton logoutBtn;
    JComboBox toWho;
    PrintWriter pout;
    BufferedReader bin;
    DefaultComboBoxModel<String> model;

    public ConstantCheck constantCheck;



    public ChatClient() {

        /*
         *      Initialize
         */
        setSize(400, 300);     // set the window size

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnOperation = new JMenu("Operation(O)");
        menuBar.add(mnOperation);

        JMenu mnConfiguration = new JMenu("Configuration(C)");
        menuBar.add(mnConfiguration);

        JMenu mnNewMenu = new JMenu("Help(H)");
        menuBar.add(mnNewMenu);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.NORTH);

        JButton userConfigBtn = new JButton("User Config");
        userConfigBtn.addActionListener(this);
        panel.add(userConfigBtn);

        JButton connectBtn = new JButton("Connect Config");
        connectBtn.addActionListener(this);
        panel.add(connectBtn);

        loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);
        panel.add(loginBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(this);
        logoutBtn.setEnabled(false);
        panel.add(logoutBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        panel.add(exitBtn);


        /*
         *          TextPanel in the middle part
         */

        JTextPane textPane = new JTextPane();
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
         *  GridBagLayout at bottom
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
        JLabel emotions = new JLabel("emotions:");
        emotions.setHorizontalAlignment(JLabel.RIGHT);
        JComboBox emoBox = new JComboBox();
        JCheckBox whisper = new JCheckBox("whisper");
        panel_1.add(sendTo, c);
        panel_1.add(toWho, c);
        panel_1.add(emotions, c);
        panel_1.add(emoBox, c);
        panel_1.add(whisper, c);

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
        c.gridx = 0;
        c.gridwidth = 5;
        c.ipadx = 400;

        showUserID.setEditable(false);
        panel_1.add(showUserID, c);



    }

    /**
     *   Action Listener to listen button
     * @param e
     */
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()){
                case "User Config":
                    UserConfig userConfig = new UserConfig();
                    userConfig.show();
                    break;
                case "Connect Config":
                    ConnectConfig connectConfig = new ConnectConfig();
                    connectConfig.show();
                    break;
                case "Login":
                    try {
                        socket = new Socket(ipAddress, portNo);
                        pout =new PrintWriter(socket.getOutputStream(), true);         //for sending things out
                        //DataInputStream in = new DataInputStream(soc.getInputStream());
                        bin = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receiveing
                        logoutBtn.setEnabled(true);
                        loginBtn.setEnabled(false);
                        pout.println("Login;;;;" + username + ";;;;");

                        String s = bin.readLine();
                        String[] strings = s.split(";;;;");
                        id = Integer.parseInt(strings[1]);

                        constantCheck = new ConstantCheck();
                        constantCheck.start();


                    }  catch (SocketTimeoutException s) {
                        System.out.println("Socket timed out!");
                    } catch (IOException s) {
                        s.printStackTrace();
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                    break;
                case "Logout":
                    try {
                        pout.println("Logout;;;;" + id + ";;;;" + username + ";;;;");
                        String s = bin.readLine();
                        String strings[] = s.split(";;;;");
                        if(strings[0].equals("Kill")){
                            bin.close();
                            pout.close();
                            socket.close();
                        }

                        logoutBtn.setEnabled(false);
                        loginBtn.setEnabled(true);

                        constantCheck.stop();
                    }catch (Exception f){
                        f.printStackTrace();
                    }
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "Send":
                    //pout.println("SendFrom;;;;" + username +";;;;" id + ";;;;" + "Recieve;;;;" + );
                    break;
                default:
                    System.out.println("It should not happened");
                    break;
            }

        }

    public static void main(String args[]) {
        ChatClient frame = new ChatClient();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
    }

    class ConstantCheck extends Thread{
        PrintWriter pout2;
        BufferedReader bin2;
        String s;
        String[] strings;
        public void run(){
            try {
                pout2 = new PrintWriter(socket.getOutputStream(), true);         //for sending things out
                //DataInputStream in = new DataInputStream(soc.getInputStream());
                bin2 = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receiveing
            }catch(Exception e){
                e.printStackTrace();
            }
            while(true){
                pout2.println("GetList");
                try {
                    s = bin2.readLine();
                    strings = s.split(";;;;");
                    if(strings[0].equals("List")){
                        for(int i = 1; i < strings.length; i++){
                            model.addElement(strings[i]);
                        }
                    }
                } catch (Exception e){
                    try {
                        bin2.close();
                        pout2.close();
                    }catch (Exception f){}
                }

                pout2.println("Check update;;;;");
                try{
                    s = bin2.readLine();
                    strings = s.split(";;;;");
                    if(strings[0].equals("Kill")){
                        bin.close();
                        pout.close();
                        bin2.close();
                        pout2.close();
                        socket.close();
                        logoutBtn.setEnabled(false);
                        loginBtn.setEnabled(true);
                        doc.insertString(0,"Server has shutted down, disconnected", null);

                    } else if(strings[0].equals("No command")){
                        //do nothing
                    } else if(strings[0].equals("Notification")){
                        doc.insertString(0,"Server has just sent you a notification", null);
                    }
                    this.sleep(1000);

                } catch (Exception e){
                    try {
                        this.join();
                    } catch (Exception f){
                        f.printStackTrace();
                    }
                }
            }
        }
    }


}

