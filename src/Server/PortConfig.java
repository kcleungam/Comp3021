package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Krauser on 5/5/2016.
 */
public class PortConfig extends JDialog implements ActionListener{
    public JTextField portField;
	public PortConfig(){
		setSize(400, 200);

		JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);


        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;


        panel_1.setLayout(g);

        c.gridy = 0;
        JLabel portNoLabel = new JLabel("Listen to Port Number:");
        portNoLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel_1.add(portNoLabel, c);
        portField = new JTextField();
        portField.setText(ChatServer.portNo);
        portField.setColumns(8);
        c.gridwidth = 3;
        panel_1.add(portField, c);

        c.gridy = 1;
        JLabel defaultMessage = new JLabel("Default Port Configuration: 8888");
        panel_1.add(defaultMessage,c);

        c.gridy = 2;
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
            ChatServer.portNo = portField.getText();
            dispose();
        }else if(e.getActionCommand().equals("Cancel")){
            dispose();
        }
    }
	
    public static void main(String args[]) {
        PortConfig frame = new PortConfig();
        frame.show();
    }
}
