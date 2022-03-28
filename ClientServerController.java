import java.io.PrintWriter;

public class ClientServerController {

    public void ServerToClientMessage(PrintWriter output, String message) {
        output.println(message);
        output.flush();
    }

    public void ClientToClientMessage(String serverReply) {

    }
}