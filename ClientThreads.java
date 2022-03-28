import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientThreads implements Runnable {

    private PrintWriter output;
    private BufferedReader input;
    private int clientNum;
    private Socket socket;
    private Thread thread;
    private Socket server;
    // private GameLogic game:
    private ClientServerController CSC;

    public ClientThreads(Socket client, /* GameLogic game, */ int clientNum) {
        // this.game = game;
        this.clientNum = clientNum;
        this.socket = client;
        CSC = new ClientServerController();
        System.out.println(client.toString());
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Stream not set up for client");
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            for (int i = 0; i < 10; i++) {
                outputMessage("Test to client : " + Integer.toString(clientNum));
                System.out.println("Test to client : " + Integer.toString(clientNum));
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected void outputMessage(String message) {
        CSC.ServerToClientMessage(output, message);
    }

}