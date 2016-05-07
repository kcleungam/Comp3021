package Server;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.io.*;
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
    public StyledDocument doc;
    public static boolean stopingServier = false;

    public static ArrayList<String> serverMessageList = new ArrayList<>();
    public ConstantSend constantSend;

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


    @Override
    public void run() {
        constantSend.start();
        running = true;
        while (running == true) {
                    try {
                        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                        Socket soc = serverSocket.accept();
                        socketArrayList.add(soc);
                        ServerThread thread = new ServerThread(soc,this,textPane);
                        thread.start();
                        threadPool.add(thread);

                        haveClient = true;
                        System.out.println("Just connected to " + soc.getRemoteSocketAddress());

                        /*
                        out[clients] = new DataOutputStream(socketArrayList[clients].getOutputStream());    //for sending things out
                        in[clients] = new DataInputStream(socketArrayList[clients].getInputStream());       //receiveing
                        Bin[clients] =new BufferedReader(new InputStreamReader(socketArrayList[clients].getInputStream()));
                        */

                    } catch (SocketTimeoutException s) {
                        System.out.println("Socket timed out!");
                        break;
                    } catch (IOException e) {

                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    try {
                        Thread.currentThread().sleep(10);
                    } catch (Exception f){
                        f.printStackTrace();
                    }
        }
    }

    public void stopServer(){
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
                Thread.currentThread().sleep(1000);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        updateClientCount();
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
    public void stopOneClient(int position){
        try {
            socketArrayList.get(position).close();
            socketArrayList.remove(position);
        } catch (Exception e){}
    }


    public void updateClientCount(){
        ChatServer.countUserLabel.setText("There are " + clientDataArrayList.size() + " users online");
    }

    public void exchangeMessage(String message){

    }

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

    class ConstantSend extends Thread{

        public void run() {
            while (true) {
                updateClientCount();

                String topMessage = "";
                if(Server.serverMessageList.size() > 0){
                    topMessage = Server.serverMessageList.get(Server.serverMessageList.size() - 1);
                    System.out.println(topMessage);
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