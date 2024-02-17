import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
 private Socket clientSocket;
 private PrintWriter out;
 private BufferedReader in;

 public void connect(String serverAddress, int serverPort) {
  try {
   clientSocket = new Socket(serverAddress, serverPort);
   out = new PrintWriter(clientSocket.getOutputStream(), true);
   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

   System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

   Thread receiveMessageThread = new Thread(() -> {
    try {
     String serverMessage;
     while ((serverMessage = in.readLine()) != null) {
      System.out.println("Received message from server: " + serverMessage);
     }
    } catch (IOException e) {
     e.printStackTrace();
    }
   });
   receiveMessageThread.start();

   BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
   String inputLine;
   while ((inputLine = consoleReader.readLine()) != null) {
    out.println(inputLine);
   }

  } catch (IOException e) {
   e.printStackTrace();
  } finally {
   try {
    if (out != null) out.close();
    if (in != null) in.close();
    if (clientSocket != null) clientSocket.close();
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }

 public static void main(String[] args) {
  String serverAddress = "192.168.24.131"; // Адрес
  int serverPort = 12345; // Порт
  Client client = new Client();
  client.connect(serverAddress, serverPort);
 }
}
