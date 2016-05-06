

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Krauser on 5/5/2016.
 *              !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *              This file is never used and I don't want to use, just for reference
 *              This file is never used and I don't want to use, just for reference
 *              !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class Client extends Thread{

    public Socket soc;
    public String ipAddress = "127.1.1.0";
    public int portNo = 8888;
    public InputStream inFromServer;
    public DataInputStream in;
    public OutputStream outToServer;
    public DataOutputStream out;
    public PrintWriter Pout;
    public FileWriter fw;

    public Client(){
        ipAddress = "127.1.1.0";
        portNo = 8888;
    }

    @Override
    public void run(){
        try {
            soc = new Socket(ipAddress, portNo);
        }  catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopClient(){
        try{
            soc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exchangeMessage(String message){

    }

    public void listen(){

    }
}
