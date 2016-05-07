package Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * Created by Krauser on 5/5/2016.
 */
public class ServerThread extends Thread{
    public Socket socket; // A connected socket
    public Server server;
    public boolean isAlive = false;
    public JTextPane textPane;
    public PrintWriter pout;
    public BufferedReader bin;
    public String serverMessage = "";
    public boolean sendDone = false;

    public ServerThread(Socket soc, Server ser, JTextPane textP) {
        this.socket = soc;
        this.server = ser;
    }

    public void run() {
        try {
            isAlive = true;
            pout =new PrintWriter(socket.getOutputStream(), true);
            //DataInputStream in = new DataInputStream(soc.getInputStream());
            bin = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receiveing


            // Continuously serve the client
            while (true) {
                String message = bin.readLine();
                String[] strings = message.split(";;;;");
                if(strings[0].equals("Logout")){
                    ChatServer.doc.insertString(0, "User: " + strings[2] + "with id:" + strings[1] + "has logout\n", null);
                    System.out.println(strings[1]);
                    System.out.println(strings[2]);
                    server.removeOnlineUser( Integer.parseInt(strings[1]), strings[2]);
                    ChatServer.doc.insertString(0, Server.clientDataArrayList.size() + " users is online\n", null);
                    pout.println("Kill");
                    server.stopOneClient(socket);
                    server.removeThread(this);
                    socket.close();
                    bin.close();
                    pout.close();
                    isAlive = false;
                    break;
                } else if(strings[0].equals("Login")){
                    int id = Server.idCount;
                    ++Server.idCount;
                    ClientData CD = new ClientData(id, strings[1]);
                    pout.println("ID" + ";;;;" + id + ";;;;");

                    Server.clientDataArrayList.add(CD);
                    ChatServer.doc.insertString(0, "User: \"" + CD.username + "\" with id: " + CD.id + " has logon\n", null);

                } else if(strings[0].equals("GetList")){
                    String temp = "";
                    temp += "List;;;;";
                    for(int i = 0; i < Server.clientDataArrayList.size(); i++){
                        temp += Server.clientDataArrayList.get(i).username + "," + Integer.toString( Server.clientDataArrayList.get(i).id );
                        temp += ";;;;";
                    }
                    pout.println(temp);

                } else if(strings[0].equals("Check update") ){
                    if(serverMessage.equals("")){
                        pout.println("No command;;;;");
                        serverMessage = "";
                        sendDone = true;
                    }else if (serverMessage.equals("Kill")){
                        pout.println("Kill");
                        serverMessage = "";
                        sendDone = true;
                        server.stopOneClient(socket);
                        server.removeThread(this);
                        socket.close();
                        bin.close();
                        pout.close();
                        isAlive = false;

                        break;
                    } else if(serverMessage.equals("Notification")){
                        pout.println("Notification");
                        serverMessage = "";
                        sendDone = true;
                    }
                }


                /*
                try {
                    Thread.currentThread().sleep(50);
                }  catch(Exception e) {
                        System.err.println(e);
                }
                */
            }

        } catch(Exception e) {
            System.err.println(e);
        }

        System.out.println("This thread is done!");
    }

    public void send(String s){
        pout.println(s);
    }

    public void setServerMessage(String s){
        serverMessage = s;
    }

}

