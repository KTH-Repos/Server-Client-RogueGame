import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.googlecode.lanterna.screen.Screen;

public class ClientThreads implements Runnable {

    private ObjectOutputStream output;
    private BufferedReader input;
    private int clientNum;
    private Socket socket;
    private Thread thread;
    private Socket server;
    private Map map;
    private ClientServerController CSC;

    public ClientThreads(Socket client, Map map, int clientNum) {
        this.map = map;
        this.clientNum = clientNum;
        this.socket = client;
        CSC = new ClientServerController();
        // super.setCSC(CSC);
        // super.setGame(game);
        System.out.println(client.toString());
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            //input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //output = new PrintWriter(new OutputStreamWrit(socket.getOutputStream()), true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Stream not set up for client");
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //for (;;) {
         outputMessage(map);
         /* System.out.println("Test to client : " + Integer.toString(clientNum));
         Thread.sleep(5000);
         } */

    }

    protected void outputMessage(Map map) {
        try {
            CSC.ServerToClientMessage(output, map);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}