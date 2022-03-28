import java.io.*;

public class ClientGUI {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private String serverIP;
    private int portValue;
    private ClientLogic clientLogic;

    public ClientGUI() {
        try {
            System.out.println("Please enter the server IP");
            serverIP = br.readLine();
            System.out.println("Please enter the port number for the server");
            portValue = Integer.parseInt(br.readLine());
            clientLogic = new ClientLogic(serverIP, portValue, this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void outPutSent(String reply) {
        System.out.println(reply);
    }

    public static void main(String[] args) {
        ClientGUI client = new ClientGUI();
    }
}