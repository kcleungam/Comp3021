package Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by Krauser on 5/5/2016.
 */
public class ServerThread extends Thread{
    public Socket socket; // A connected socket
    public Server server;
    public boolean isAlive = false;

    public ServerThread(Socket soc, Server ser) {
        this.socket = soc;
        this.server = ser;
    }

    public void run() {
        try {
            System.out.println("ThreadStartSuccessfully");
            isAlive = true;
            PrintWriter pout =new PrintWriter(socket.getOutputStream(), true);
            //DataInputStream in = new DataInputStream(soc.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream())); //receiveing


            // Continuously serve the client
            while (true) {
                String message = bin.readLine();
                System.out.println("!!!!!!" + message);

                String[] strings = message.split(";;;;");
                System.out.println("Split successful?" + strings[0]);
                if(strings[0].equals("Kill")){
                    pout.println("Kill");
                    server.stopOneClient(socket);
                    server.removeThread(this);
                    socket.close();
                    bin.close();
                    pout.close();
                    isAlive = false;
                    System.out.println(server.threadPool.size());
                    break;
                }
                try {
                    Thread.currentThread().sleep(50);
                }  catch(Exception e) {
                        System.err.println(e);
                    }
            }

        } catch(IOException e) {
            System.err.println(e);
        }

        System.out.println("This thread is done!");
    }


}

