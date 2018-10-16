import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Runnable {

  private ServerSocket serverSocket;

  private Server() {
    this(8080);
  }

  private Server(int port) {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server is opened on port " + port);
    } catch (IOException e) {
      System.out.println("Error when opening socket on " + port + " : " + e.getMessage());
    }
  }

  //Server Starting Point
  public static void main(String[] args) {
    Server server = new Server();
    new Thread(server).start();
  }

  @Override
  public void run() {
    while(!Thread.currentThread().isInterrupted()){
      try {
        Socket socket = serverSocket.accept();
        Connection connection = new Connection(socket);
        new Thread(connection).start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}