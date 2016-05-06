package Server;

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

    public Server() {
        try {
            portNo = Integer.parseInt(ChatServer.portNo);
            serverSocket = new ServerSocket(portNo);
            serverSocket.setSoTimeout(1000000);
            serverName = "ChatServer";
            countOnline = 0;
            Ready = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;
        while (running == true) {
                    try {
                        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                        Socket soc = serverSocket.accept();
                        socketArrayList.add(soc);
                        ServerThread thread = new ServerThread(soc,this);
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
        running = false;
        try {
            for(int i = socketArrayList.size() - 1; i >= 0 ; i--){
                try {
                    socketArrayList.get(i).close();
                    socketArrayList.remove(i);
                } catch (SocketTimeoutException s) {
                    System.out.println("Socket timed out!");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
            this.stop();
        } catch (Exception e){
            e.printStackTrace();
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
    public void stopOneClient(int position){
        try {
            socketArrayList.get(position).close();
            socketArrayList.remove(position);
        } catch (Exception e){}
    }

    public void listen(){

        for(int i = 0; i < socketArrayList.size(); i++){
            try {
                String s = binArray.get(i).readLine();
            } catch (Exception e){
                e.printStackTrace();
            }

        }

        /*              Listen input
         for(int j=0;j<clients;j++){
            if(generating[j] == true) {
                if(count % freq == 0) {
		    String s = Bin[j].readLine();
                    if( s.equals("false")){	//Remember everytime readLine will clear buffer, read again will fail
				System.out.println("Shutting Down");
				clients--;
				soc[j].close();
				if(clients == 0){
					haveClient = false;
					shutdown = true;
				}
		    }else{
			System.out.println("Sent");
		    }
                }
            }
        }


        for(int j=0 ;j<clients;j++){
            if(generating[j] == true) {
                if(count % freq == 0) {
                    out[j].writeInt(Math.abs(random.nextInt()% 100));           //Send things out
                    out[j].flush();
                }
            }
        }

        */
    }

    public int getClientCount(){
        return socketArrayList.size();
    }

    public void exchangeMessage(String message){

    }

    public void removeThread(ServerThread serverThread){
        threadPool.remove(serverThread);
    }

}