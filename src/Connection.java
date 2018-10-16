import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


class Connection implements Runnable {

  private final Socket socket;
  private PrintWriter printWriter;
  private BufferedReader bufferedReader;
  private DataInputStream dataInputStream;


  public Connection(Socket socket) {
    this.socket = socket;
    initConnection();
  }

  private void initConnection() {
    try {
      printWriter = new PrintWriter(socket.getOutputStream());
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      dataInputStream = new DataInputStream(socket.getInputStream());
    } catch (IOException e) {
      System.err.println("Impossible d'ouvrir les buffers in et out");
    }
  }

  private void closeConnection() {
    try {
      printWriter.close();
      bufferedReader.close();
      dataInputStream.close();
      socket.close();
    } catch (IOException e) {
      System.err.println("Erreur lors de la fermeture de la connexion avec le client");
    }
  }


  @Override
  public void run() {
    try {
      //Read the command and the parameters
      String req = bufferedReader.readLine();
      System.out.println("La requête reçue du client est :");
      System.out.println(req);

      ServerRequest request = new ServerRequest(printWriter, dataInputStream, bufferedReader, req);
      request.respond();
      closeConnection();
    } catch (SocketTimeoutException e) {
      System.err.println("Time Out : " + e.getMessage());
    } catch (IOException e) {
      System.err.println("Error : " + e.getMessage());
    }
  }
}