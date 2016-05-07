package Server;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by Krauser on 5/5/2016.
 */
public class Server extends Thread{
    public String serverName = "ChatServer";
    public int portNo = 8888;
    public int countOnline = 0;
    public boolean haveClient = false;
    public ServerSocket serverSocket;
    public ArrayList<DataOutputStream> outArray = new ArrayList<>();
    public ArrayList<DataInputStream> inArray = new ArrayList<>();
    public ArrayList<BufferedReader> binArray = new ArrayList<>();
    public static boolean Ready = false;

    public static boolean running = false;
    public ArrayList<Socket> socketArrayList = new ArrayList<>();
    public ArrayList<ServerThread> threadPool = new ArrayList<>();

    public static ArrayList<ClientData> clientDataArrayList;
    public static int idCount = 0;

    public JTextPane textPane;
    public static boolean stopingServier = false;

    public static ArrayList<String> serverMessageList = new ArrayList<>();
    public ConstantSend constantSend;
    public static ArrayList<Message> whisperArrayList = new ArrayList<>();
    public static ArrayList<Message> messageArrayList = new ArrayList<>();

    public Server(JTextPane textP) {
        try {
            textPane = textP;
            portNo = Integer.parseInt(ChatServer.portNo);
            serverSocket = new ServerSocket(portNo);
            serverSocket.setSoTimeout(1000000);
            serverName = "ChatServer";
            countOnline = 0;
            Ready = false;
            constantSend = new ConstantSend();
            clientDataArrayList = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *          This is a thread which wait for connection of Client, once a Client comes, pass it to a new thread
     *          Then wait for another Client
     */
    @Override
    public void run() {
        constantSend.start();
        running = true;
        while (running == true) {
                    try {
                        ChatServer.doc.insertString(0, "Waiting for client on port " + serverSocket.getLocalPort() + "..." , null);
                        Socket soc = serverSocket.accept();
                        socketArrayList.add(soc);
                        ServerThread thread = new ServerThread(soc,this,textPane);
                        thread.start();
                        threadPool.add(thread);

                        haveClient = true;
                        ChatServer.doc.insertString(0, "Just connected to " + soc.getRemoteSocketAddress() ,null);

                    } catch (SocketTimeoutException s) {
                        System.out.println("Socket timed out!");
                        try {
                            ChatServer.doc.insertString(0, "Socket timed out!", null);
                        } catch (Exception f){
                            f.printStackTrace();
                        }
                        break;
                    } catch (IOException e) {

                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (Exception f){
                        f.printStackTrace();
                    }
        }
    }

    /**
     *          This function first send the stopping message to all client and make sure all thread has sent it
     *          Then stop the socket and server
     */
    public void stopServer(){
        try {
            ChatServer.doc.insertString(0, "Stoping Server, please wait until all client disconnected!", null);
        }catch (Exception f){
            f.printStackTrace();
        }
        stopingServier = true;
        serverMessageList.add("Kill");

        while (true){
            if(threadPool.size() == 0){
                try{
                    serverSocket.close();
                    this.stop();
                    break;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            try{
                Thread.currentThread().sleep(2000);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        whisperArrayList.clear();
        updateClientCount();
        try {
            ChatServer.doc.insertString(0, "Server has stopped", null);
        }catch (Exception g){
            g.printStackTrace();
        }
    }

    public void setPortNo(String port){
        portNo = Integer.parseInt(port);
    }

    public void stopOneClient(Socket soc){
        for (int i = socketArrayList.size() -1 ; i >= 0 ; i--){
            if(socketArrayList.get(i).equals(soc)){
                try {
                    socketArrayList.get(i).close();
                    socketArrayList.remove(i);
                } catch (Exception e){}
            }
        }
    }

    /**
     *          update the  online user number in countUserLabel
     */
    public void updateClientCount(){
        ChatServer.countUserLabel.setText("There are " + clientDataArrayList.size() + " users online");
    }


    /**
     *          Remove a thread from threadPool which handle Clients
     * @param serverThread
     */
    public void removeThread(ServerThread serverThread){
        threadPool.remove(serverThread);
    }

    public void removeOnlineUser(int id, String username){
        for(int i = clientDataArrayList.size() - 1; i >= 0 ; i--){
            if(clientDataArrayList.get(i).id == id && clientDataArrayList.get(i).username.equals(username)){
                clientDataArrayList.remove(i);
            }
        }
    }


    /**
     *          Return the name of user given with id
     * @param id
     * @return
     */
    public String getNameByID(int id){

        for(int i = 0; i < clientDataArrayList.size(); i++){
            if(clientDataArrayList.get(i).id == id){
                return clientDataArrayList.get(i).username;
            }
        }
        return null;
    }

    /**
     *      Check whether someone whisper to client
     * @param id
     * @return
     */
    public boolean checkWhisper(int id){
        boolean temp = false;
        for(int i = 0; i < whisperArrayList.size(); i++){
            if(whisperArrayList.get(i).toID == id){
                temp = true;
            }
        }
        return temp;
    }

    /**
     *      Pop a message our from arrayList
     * @param toID
     * @return
     */
    public String popWhisper(int toID){
        String temp = "No command;;;;";
        for(int i = 0; i < whisperArrayList.size(); i++){
            if(whisperArrayList.get(i).toID == toID){
                temp = "WhisperMessage;;;;"  + getNameByID(whisperArrayList.get(i).fronID) + ";;;;" + whisperArrayList.get(i).fronID +";;;;"
                          + getNameByID(whisperArrayList.get(i).toID) +";;;;" + whisperArrayList.get(i).toID + ";;;;"
                        + whisperArrayList.get(i).content + ";;;;";
                whisperArrayList.remove(i);
                break;
            }
        }
        return temp;
    }

    /**
     *      Check whether someone give a message to client
     * @param toID
     * @return
     */

    public boolean checkMessage(int toID){
        boolean temp = false;
        for(int i = 0; i < messageArrayList.size(); i++){
            if(messageArrayList.get(i).toID == toID){
                temp = true;
            }
        }
        return temp;
    }


    /**
     *      Pop a message our from arrayList
     * @param toID
     * @return
     */
    public String popMessage(int toID) {
        String temp = "No command;;;;";
        for (int i = 0; i < messageArrayList.size(); i++) {
            if (messageArrayList.get(i).toID == toID) {
                temp = "Send;;;;" + messageArrayList.get(i).content + ";;;;";
                messageArrayList.remove(i);
                break;
            }
        }
        return temp;
    }


    /**
     *      I use a thread to handle some systematic things such as constantly update the list of Online user to Client
     */
    class ConstantSend extends Thread{

        public void run() {
            while (true) {
                updateClientCount();

                String topMessage = "";
                if(Server.serverMessageList.size() > 0){
                    topMessage = Server.serverMessageList.get(Server.serverMessageList.size() - 1);
                    Server.serverMessageList.remove(Server.serverMessageList.size() - 1);
                }
                for (ServerThread st : threadPool) {
                    st.setServerMessage(topMessage);
                }
                while (true) {
                    boolean sendDone = true;
                    for (ServerThread st : threadPool) {
                        if (st.sendDone == false) {
                            sendDone = false;
                        }
                    }
                    if (sendDone == true) {
                        break;
                    }
                    try {
                        this.sleep(500);
                    } catch (Exception e) {
                        try {
                            this.join();
                        } catch (Exception f) {
                            f.printStackTrace();
                        }
                    }

                }

                String temp = "";
                temp += "List;;;;";
                for(int i = 0; i < Server.clientDataArrayList.size(); i++){
                    temp += Server.clientDataArrayList.get(i).username + "," + Integer.toString( Server.clientDataArrayList.get(i).id );
                    temp += ";;;;";
                }
                String[] strings = temp.split(";;;;");
                if(strings[0].equals("List")){
                    for(int i = 1; i < strings.length; i++){
                        boolean exist = false;
                        for(int j = 0; j < ChatServer.model.getSize(); j++){
                            if(ChatServer.model.getElementAt(j).equals(strings[i])){
                                exist = true;               // Check if the element exists
                            }
                        }
                        if(exist == false){
                            ChatServer.model.addElement(strings[i]);   // add the new element if it is not exist
                        }
                    }
                }



                try {
                    this.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        this.join();
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }

            }
        }
    }

}