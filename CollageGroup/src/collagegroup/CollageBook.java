package collagegroup;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 *
 * @author krButani
 */
public class CollageBook extends Applet implements ActionListener {
    
    Label lblusername, lblport;
    TextField txtusername;
    Button btnusername;
    ChatBox ch;
    
    public void init() {
        
        
        
        lblusername = new Label("Enter Username: ");
        txtusername = new TextField();
        btnusername = new Button("Login");
        try {
            lblport = new Label(InetAddress.getLocalHost().toString());
        } catch (Exception e) {
        }
        
        
        setLayout(null);
        
        lblusername.setBounds(10, 5, 250, 40);
        txtusername.setBounds(10, 45, 250, 35);
        btnusername.setBounds(270, 45, 150, 35);
        lblport.setBounds(10, 100, 300, 35);
        
        
        add(lblusername);
        add(txtusername);
        add(btnusername);
        add(lblport);
        btnusername.addActionListener(this);
        setFont(new Font("Verdana", Font.PLAIN, 24));
        setSize(430, 200);
        ch = new ChatBox(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().toString().equalsIgnoreCase("Login")) {
            ch.setVisible(true);
            ch.mname = txtusername.getText();
            ch.setData();
            this.setEnabled(false);
        }
        
    }
}
