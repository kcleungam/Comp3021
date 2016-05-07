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

    /**
     *          This is a worker thread which keep handling the request from Client
     */
    public void run() {
        try {
            isAlive = true;
            pout =new PrintWriter(socket.getOutputStream(), true);
            //DataInputStream in = new DataInputStream(soc.getInputStream());
            bin = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receiveing


            // Continuously serve the client
            while (true) {
                String readLine = bin.readLine();
                String[] strings = readLine.split(";;;;");

                // handle different requests
                if(strings[0].equals("Logout")){
                    ChatServer.doc.insertString(0, "User: " + strings[2] + "with id:" + strings[1] + "has logout\n", null);
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

                } else if(strings[0].equals("Whisper")){

                    int fromID = Integer.parseInt(strings[1]);
                    int toID = Integer.parseInt(strings[2]);
                    String content = strings[3];
                    Message message = new Message(fromID, toID, content);
                    Server.whisperArrayList.add(message);
                    String show = strings[1] + "whisper to " + strings[2] + ":" + strings[3];
                    ChatServer.doc.insertString(0, show, null);

                } else if(strings[0].equals("Send")){

                    int fromID = Integer.parseInt(strings[1]);
                    String content = strings[2];
                    for(int i = 0; i < Server.clientDataArrayList.size(); i++){
                        Message message1 = new Message(fromID, Server.clientDataArrayList.get(i).id, content);
                        Server.messageArrayList.add(message1);
                    }
                    String show = strings[2];
                    ChatServer.doc.insertString(0, show, null);

                }else if(strings[0].equals("Check update") ){

                    if(serverMessage.equals("")){
                        if(Server.whisperArrayList.size() > 0 ){
                            if ( server.checkWhisper( Integer.parseInt(strings[1]) ) ){
                                pout.println(server.popWhisper( Integer.parseInt(strings[1]) ));
                                serverMessage = "";
                                sendDone = true;
                            } else {
                                pout.println("No command;;;;");
                                serverMessage = "";
                                sendDone = true;
                            }
                            continue;
                        } else if(Server.messageArrayList.size() > 0 ){
                            if(server.checkMessage(Integer.parseInt(strings[1] ) ) ){
                                pout.println(server.popMessage(Integer.parseInt(strings[1] ) ) );
                                serverMessage = "";
                                sendDone = true;
                            }
                            continue;
                        } else {
                            pout.println("No command;;;;");
                            serverMessage = "";
                            sendDone = true;
                        }
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
                } else{
                    System.out.println("Fail to capture message");
                }


            }

        } catch(Exception e) {
            System.err.println(e);
        }

    }

    public void setServerMessage(String s){
        serverMessage = s;
    }

}

