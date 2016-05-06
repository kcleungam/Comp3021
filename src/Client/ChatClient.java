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
    public static String username = "hkuster";
    public static String ipAddress = "127.1.1.0";
    public static int portNo = 8888;
    public Socket socket;

    public static JTextField showUserID = new JTextField("Your name is:" + username);
    JButton loginBtn;
    JButton logoutBtn;
    PrintWriter pout;
    BufferedReader bin;

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
        StyledDocument doc = textPane.getStyledDocument();


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
        JComboBox toWho = new JComboBox();
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
                        System.out.println("Here?");
                        pout.println("Kill");
                        String s = bin.readLine();
                        if(s.equals("Kill")){
                            bin.close();
                            pout.close();
                            socket.close();
                            System.out.println("Closed");
                        }

                        logoutBtn.setEnabled(false);
                        loginBtn.setEnabled(true);
                    }catch (Exception f){
                        f.printStackTrace();
                    }
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "Send":

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


}

