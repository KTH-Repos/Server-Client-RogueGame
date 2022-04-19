import java.io.PrintWriter;

public class ClientServerController {

    public void ServerToClientMessage(PrintWriter output, String message) {
        output.println(message);
        output.flush(); // send the message immediately
    }

    public void ClientToClientMessage(String serverReply) {
        // clientGUI.OutputSent(serverReply);
    }
}