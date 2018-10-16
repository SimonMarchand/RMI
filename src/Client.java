import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
  //Connection parameters
  private final String address;
  private final int port;
  private Socket socket;

  //IO communication
  private Scanner scanner;
  
  private ClientRequest request;


  private Client(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public static void main(String[] args) {
    Client s = new Client("127.0.0.1", 8080);
    new Thread(s).start();
  }

  @Override
  public void run() {
    initRequest();
    String answer = ask();
    System.out.println("Réponse du serveur : " + answer);
    closeConnection();
  }

  private void initConnectionAndCommunication() {
    try {
      socket = new Socket(InetAddress.getByName(address), port);
      request.printWriter = new PrintWriter(socket.getOutputStream());
      request.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      request.dataOutputStream= new DataOutputStream(socket.getOutputStream());
      scanner = new Scanner(System.in);
    } catch (IOException e) {
      System.err.println("Impossible de se connecter à l'adresse " + address + ":" + port + " : ");
    }
  }

  private void initRequest() {
    request = new ClientRequest();
    initConnectionAndCommunication();
    chooseProtocol();
    request.initWithAssistance();
  }

  private void chooseProtocol() {
    System.out.println("Choisir un protocol : OBJECTColl (1), BYTEColl (2) et SOURCEColl (3)");
    switch (scanner.nextLine()) {
      case "1":
        request.strategy = new ObjectColl(request);
        break;
      case "2":
        request.strategy = new ByteColl(request);
        break;
      case "3":
        request.strategy = new SourceColl(request);
        break;
      default:
        chooseProtocol();
        break;
    }
    request.strategyName = request.strategy.toString();
  }
  
  /***
   * Ask the request to the server
   */
  private String ask() {
    sendRequest(request);
    return request.strategy.executeAsClient();
  }

  private void sendRequest(Request request) {
    this.request.printWriter.println(request.toString());
    this.request.printWriter.flush();
  }

  private void closeConnection() {
    try {
      this.request.printWriter.close();
      this.request.bufferedReader.close();
      this.request.dataOutputStream.close();
      socket.close();
    } catch (IOException e) {
      System.err.println("Erreur lors de la fermeture de la connexion avec le serveur");
      e.printStackTrace();
    }
  }
}