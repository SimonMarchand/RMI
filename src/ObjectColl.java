import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

public class ObjectColl extends StrategyToString {

  public ObjectColl(ClientRequest request) {
    super(request);
  }

  public ObjectColl(ServerRequest request) {
    super(request);
    initialiseRequest();
  }

  @Override
  public String executeAsClient(Request clientRequest) {
    this.request.fileFolder = "src";
    this.request.fileExtension = ".obj";
    serialize(new Calc());
    sendFileLength();
    sendFile();
    return waitingAnswer();
  }

  @Override
  public void executeAsServer(Request serverRequest) {
    Object object;
    this.request.fileFolder = "server";
    this.request.fileExtension = ".obj";
    saveFile();
    object = deserialize();
    sendAnswer(object.getClass(), object);
  }

  @Override
  public String toString() {
    return "OBJECTColl";
  }
}
