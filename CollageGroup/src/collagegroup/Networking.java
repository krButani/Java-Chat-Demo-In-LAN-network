/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collagegroup;

import java.net.*;

/**
 *
 * @author krButani
 */
public class Networking {
    
    String str[],names[];
    int connect[],oport[];
    DatagramSocket ds;
    public Networking() {
        str = new String[120];
        oport = new int[120];
        names = new String[120];
        connect = new int[120];
        
        for(int i=1,j=1;i<=120;i+=2,j++) {
            str[i-1] = "192.168.151." + j; 
            oport[i-1] = 8000 + i;
            str[i] = "192.168.152."+ j;
            oport[i] = 8000 + i+1;
            connect[i-1] = connect[i] =  0;
            names[i-1] = names[i] = "";
        }
    }
    
    public int getPort(String strBut) {
        int port = 0;
        for(int i=0;i<120;i++) {
            if(str[i].equalsIgnoreCase(strBut)){
                port = oport[i];
                break;
            }
        }
        return port;
    }
    
    public int getPortNAME(String strBut) {
        int port = 0;
        for(int i=0;i<120;i++) {
            if(names[i].equalsIgnoreCase(strBut)){
                port = oport[i];
                break;
            }
        }
        return port;
    }
    
    public void sendMSG(String msg,int uport,boolean flag) throws Exception {
       
        DatagramPacket ps = new DatagramPacket(msg.getBytes(), msg.getBytes().length);
        for(int i=0;i<120;i++) {
            if((flag == false && connect[i] == 1) || (flag == true && uport == oport[i] && connect[i] == 1)) {
                try {
                    ps.setAddress(InetAddress.getByName(str[i]));
                    ps.setPort(oport[i]);
                    ps.setData(msg.getBytes());
                    ds.send(ps);
                    System.out.println(""+i);
                    break;
                } catch(Exception e) {
                    System.out.println("Error : sendMessage");
                }    
            }
        }
    }
    
    public void sendConnect(String msg,int port) {
            
            DatagramPacket ps = new DatagramPacket(msg.getBytes(), msg.getBytes().length);
           
            for(int i=0;i<120;i++) {
                try {
                    if(oport[i] != port) {
                        ps.setAddress(InetAddress.getByName(str[i]));
                        ps.setPort(oport[i]);
                        ps.setData(msg.getBytes());
                        ds.send(ps);
                    }
                    //System.out.println(""+i);
                } catch(Exception e) {
                    System.out.println("Error : sendConnection function");
                }    
           }
    }
    
    public void disconnectMsg(String str1) {
        
        DatagramPacket ps = new DatagramPacket("Dis:Disconnect".getBytes(), "Dis:Disconnect".getBytes().length);
        for(int i=0;i<120;i++) {
            if(str1.equalsIgnoreCase(names[i])) {
                connect[i] = 0;
                try {
                    ps.setAddress(InetAddress.getByName(str[i]));
                    ps.setPort(oport[i]);
                    ps.setData("Dis:Disconnect".getBytes());
                    ds.send(ps);
                    break;
                } catch(Exception e) {
                    System.out.println("Error: Disconnect MSG Faild.");
                }
            }
        }
        
    }
    
    public String getName(int port) {
        String namese = "";
        
        for(int i=0;i<120;i++) {
            if(port == oport[i]) {
                namese = names[i];
            }
        }
        
        return namese;
    }
    
    public String getNames(int port) {
        String namese = "";
        
        for(int i=0;i<120;i++) {
            if(port == oport[i]) {
                namese = names[i];
                break;
            }
        }
        
        return namese;
    }
    
    public boolean checkConnect(int port) {
        boolean f = false;
         for(int i=0;i<120;i++) {
            if(port == oport[i] && connect[i] ==1) {
                f=true;
                break;
            }
        }
        return f;
    }
    
    public void setName(String str,int port) {
        
        for(int i=0;i<120;i++) {
            if(port == oport[i]) {
                names[i] = str;
                break;
            }
        }
    }
    
    /* Resiving message code */
    
    public void connectUser(String name, String ip, int port) {
        for(int i = 0;i<120;i++) {
            if(port == oport[i]) {
                names[i] = name;
                str[i] = ip;
                connect[i] = 1;
                break;
            }
        }
    }
    
    public String disconnectME(int port) {
         String namese = "";
        
        for(int i=0;i<120;i++) {
            if(port == oport[i]) {
                namese = names[i];
                connect[i] = 0;
                break;
            }
        }
        
        return namese;
    }
}