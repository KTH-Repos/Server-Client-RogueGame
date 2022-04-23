import java.io.*;
import java.net.*;

public class ClientLogic implements Runnable {

    private PrintWriter output;
    private ObjectInputStream input;
    private ClientGUI clientGUI;
    private Socket socket;
    private Thread thread;
    private Socket server;
    private ClientServerController CSC;

    public ClientLogic(String Name, int Port, ClientGUI clientGUI) throws IOException {
        server = new Socket(Name, Port);
        output = new PrintWriter(server.getOutputStream(), false);
        input = new ObjectInputStream(server.getInputStream());
        CSC = new ClientServerController();
        this.clientGUI = clientGUI;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            waitForServer();
        } catch (IOException | RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void waitForServer() throws IOException, SocketException, RuntimeException {
        while (true) {
            Map map;
            try {
                map = (Map) input.readObject();
                Game game = new Game();
                game.start(map);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            //CSC.ClientToClientMessage(clientGUI, serverReply);
        }
    }

}
