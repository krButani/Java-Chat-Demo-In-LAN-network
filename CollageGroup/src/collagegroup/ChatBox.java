
package collagegroup;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 *
 * @author krButani
 */
class myWindowAdapter extends WindowAdapter {
       ChatBox ch;
       myWindowAdapter(ChatBox e) {
           ch = e;
           
       }
       @Override
       public void windowClosing(WindowEvent e) {
             ch.setVisible(false);
       }
    }
public class ChatBox extends Frame implements ActionListener,Runnable{
    
    CollageBook book;
    
    Label lblchatbox,lblselected;
    TextArea txtchat,txtmsg;
    Button btnmsg,btnconnecme,btndisconnect,btnmsgall;
    List lstusers;
    
    Thread t;
    
    /* User name and portno */
    String mname;
    int uport;
    
    InetAddress ia;
    Networking n;
    
   
    
    public ChatBox(CollageBook b) {
       
       book = b;
       book.setVisible(false);
       setSize(800, 670);
       addWindowListener(new myWindowAdapter(this));
       
       
       /* Set name And Port */
        
      // mname = book.txtusername.getText();
       System.out.println(mname);
       n = new Networking();
               
       /* Intisilization object. */
       lblchatbox = new Label("ChatBox: ");
       txtchat = new TextArea();
       txtmsg = new TextArea();
       btnmsg = new Button("Send");
       btnconnecme = new Button("Connect ME");
       btndisconnect = new Button("Disconnect");
       lblselected = new Label("Only Selected");
       btnmsgall = new Button("Send All");
       lstusers = new List();
       lstusers.setMultipleMode(true); 
       
       try{
            String str = InetAddress.getLocalHost().toString().split("/", 2)[1];
            uport = n.getPort(str);
            n.ds = new DatagramSocket(uport);
       } catch(Exception e){
           System.out.println("Error: User Port Selection");
       }
       
       
       /* Frame Designing */
       setFont(new Font("Verdana",Font.PLAIN,24));
       lblchatbox.setBounds(20, 40, 150, 40);
       lblselected.setBounds(430, 560, 170, 40);
       txtchat.setBounds(20,90,560,400);
       txtmsg.setBounds(20,500,400,150);
       btnmsg.setBounds(430, 610, 150, 40);
       btnconnecme.setBounds(610,610,170,40);
       btndisconnect.setBounds(610, 560, 170, 40);
       lstusers.setBounds(610,40,170,500);
       btnmsgall.setBounds(430,510,170,40);
       
       txtchat.setEditable(false);

       this.setLayout(null);
       this.add(lblchatbox);
       this.add(lblselected);
       this.add(txtchat);
       this.add(txtmsg);
       this.add(btnmsg);
       this.add(btnconnecme);
       this.add(btndisconnect);
       this.add(lstusers);
       this.add(btnmsgall);
       
       this.btnconnecme.addActionListener(this);
       this.btndisconnect.addActionListener(this);
       this.btnmsg.addActionListener(this);
       this.btnmsgall.addActionListener(this);
       
       t = new Thread(this,"MyChatBox");
       t.start();
    }
    
    public void setData() {
        n.setName(mname,uport);
            System.out.println("Try: " + mname);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equalsIgnoreCase("Connect ME")) {
            try {
                //System.out.println("Connect ME : " +mname);
                //System.out.println("Work list : " +n.getNames(uport));
                String msg = "Name:" + n.getNames(uport) + ":IP:" + InetAddress.getLocalHost().toString().split("/", 2)[1] + ":port:" + uport + ":Msg:First";
                n.sendConnect(msg,uport);
            } catch(Exception es) {
                System.out.println("Error : on send connection and fetch ip and port");
            }
            
        } else if(e.getActionCommand().equalsIgnoreCase("Send All")) {
            
            String smsg = txtmsg.getText();
            if(!(smsg.trim().equalsIgnoreCase(""))) {
                try {
                    n.sendMSG("Send:" + smsg, 0000, false);
                    String str = " ME: " + smsg + "\n";
                    
                    txtchat.append(str);
                    txtmsg.setText("");
                  
                } catch (Exception ex) {
                    System.out.println("Error on : n.sendMSG(smsg, uport)");
                }
            }
            
        } else if(e.getActionCommand().equalsIgnoreCase("Send")) {
           
            String str[] = lstusers.getSelectedItems();
            
            for(int i=0;i<=str.length;i++){
                        String smsg = txtmsg.getText();
            
                        if(!(smsg.trim().equalsIgnoreCase(""))) {
                            try {
                                    n.sendMSG("Send:" + smsg, n.getPortNAME(str[i]), true);
                                    String strq = " ME : " + smsg + "\n";
                                    txtchat.append(strq);
                                    txtmsg.setText("");

                            } catch (Exception ex) {
                                    System.out.println("Error on : n.sendMSG(smsg, uport)");
                            }
                        }
            }
            
        } else if(e.getActionCommand().equalsIgnoreCase("Disconnect")) {
            
            String str[] = lstusers.getSelectedItems();
            
            for(int i=0;i<str.length;i++){
                   n.disconnectMsg(str[i]);
                   lstusers.remove(str[i]);
            }
            
        }
        
    }

    @Override
    public void run() {
        
         try {
                
             DatagramPacket pst;
                
             pst = new DatagramPacket(new byte[5000],5000);
                
             while(true) {
                System.out.println("sdf");
                        
                n.ds.receive(pst);
                System.out.println(pst.getAddress());
                byte a[] = pst.getData();
                        
                System.out.println("Wel");
                String str = new String(a,0,pst.getLength());
                
                
                
                // Dis, Name, Send
                
                String arrstr[] = str.split(":");
                
                for(int i=0;i<arrstr.length;i++) {
                    System.out.println(arrstr[i]);
                }
                
                if(arrstr[0].equalsIgnoreCase("Name") && !(arrstr[7].equalsIgnoreCase("Second"))) {
                    if(n.checkConnect(pst.getPort()) == false && n.checkConnect(uport) == false) {
                        n.connectUser(arrstr[1],arrstr[3],Integer.parseInt(arrstr[5]));
                        
                        if(arrstr[7].equalsIgnoreCase("First")) {
                            String msg = "Name:" + n.getNames(uport) + ":IP:" + InetAddress.getLocalHost().toString().split("/", 2)[1] + ":port:" + uport + ":Msg:Second";
                            n.sendConnect(msg,uport);
                            lstusers.add(arrstr[1]);
                        }
                   }
                } 
                
                if(arrstr[0].equalsIgnoreCase("Dis")) {
                    String name = n.disconnectME(pst.getPort());
                    lstusers.remove(name);
                } 
                
                if(arrstr[0].equalsIgnoreCase("Send")) {
                    String name = n.getName(pst.getPort());
                    str = " " + name + ": " + arrstr[1] +"\n";
                    txtchat.append(str);
                }
                
                //Thread.sleep(1000);
            }
         } catch (Exception ex) {
             System.out.println("Error on: Reciving.");
         }
        
    }
    
}
 
