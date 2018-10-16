import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.PrintWriter;

/***
 * Request helper for Server
 */
public class ServerRequest extends Request {

  public ServerRequest(PrintWriter printWriter, DataInputStream dataInputStream, BufferedReader bufferedReader, String line) {
    this.printWriter = printWriter;
    this.dataInputStream = dataInputStream;
    this.bufferedReader = bufferedReader;
    rawRequest = line;
    initFromRawRequest();
  }

  /***
   * Handle the request and send the result
   */
  public void respond() {
    switch (strategyName) {
      case "OBJECTColl":
        strategy = new ObjectColl(this);
        break;

      case "BYTEColl":
        strategy = new ByteColl(this);
        break;

      case "SOURCEColl":
        strategy = new SourceColl(this);
        break;

      default:
        System.err.println("No strategyName identified");
    }
    strategy.executeAsServer();
  }
}
