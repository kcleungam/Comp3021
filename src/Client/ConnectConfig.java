package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Krauser on 5/5/2016.
 */
public class ConnectConfig extends JDialog implements ActionListener{

    public JTextField ipField;
    public JTextField portField;
	public String ip;
    public String port;

    /**
     *          The JDialog which help user to change connect config, nothing special
     * @param ip
     * @param port
     */

	public ConnectConfig(String ip, String port) {
		this.ip = ip;
        this.port = port;
        setSize(400, 200);
		
		JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);


        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;


        panel_1.setLayout(g);

        c.gridy = 0;
        JLabel ipLabel = new JLabel("Please Input Server IP:");
        ipLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel_1.add(ipLabel, c);
        
        ipField = new JTextField();
        ipField.setText(ip);
        ipField.setColumns(10);
        c.gridwidth = 3;
        panel_1.add(ipField, c);
        
        c.gridy = 1;
        JLabel portNo = new JLabel("Server Port Number	:");
        portNo.setHorizontalAlignment(JLabel.RIGHT);
        panel_1.add(portNo, c);
        portField = new JTextField();
        portField.setText(port);
        portField.setColumns(8);
        c.gridwidth = 3;
        panel_1.add(portField, c);
        
        c.gridy = 2;
        JLabel defaultMessage = new JLabel("Default Connection Configuration: 127.0.0.1");
        panel_1.add(defaultMessage,c);
        
        c.gridy = 3;
        JButton saveButton = new JButton();
        saveButton.setText("Save");
        JButton cancelButton = new JButton();
        cancelButton.setText("Cancel");
        
        panel_1.add(saveButton, c);
        panel_1.add(cancelButton, c);

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
	}

        public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Save")){
                    ChatClient.portNo = Integer.parseInt(portField.getText());
                    ChatClient.ipAddress = ipField.getText();
                    dispose();
                }else if(e.getActionCommand().equals("Cancel")){
                        dispose();
                }
        }
	


}
