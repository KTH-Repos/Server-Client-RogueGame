import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

public class ServerLogic {
    private int portValue;
    private ServerSocket socket;
    // static GameLogic game = null;

    public ServerLogic(int portValue) {
        this.portValue = portValue;
        try {
            socket = new ServerSocket(portValue);
            System.out.println("Server started!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void waitForPlayers() throws IOException {
        int clientNum = 1;
        while (true) {
            Socket client = socket.accept();
            System.out.println("Client arrived");
            System.out.println("Start thread for " + clientNum);
            // ClientThread task = new ClientThread(Client, game, ClientNum);
            clientNum++;
            // new Thread(task).start();
        }
    }

    public void kill() {
        try {
            socket.close();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Could not Close Serversocket");
        }
    }
}