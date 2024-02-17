import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    private String nickname;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void run() {
        try {
            out.println("Enter your nickname:");
            nickname = in.readLine();
            server.broadcastMessage(nickname + " has joined the chat", null);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received message from " + nickname + ": " + clientMessage);
                server.broadcastMessage(nickname + ": " + clientMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (clientSocket != null) clientSocket.close();
                server.removeClient(this);
                server.broadcastMessage(nickname + " has left the chat", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
