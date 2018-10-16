import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

public class SourceColl extends StrategyToString {

  public SourceColl(ClientRequest request) {
    super(request);
  }

  public SourceColl(ServerRequest request) {
    super(request);
    initialiseRequest();
  }

  @Override
  public String executeAsClient(Request request) {
    this.request.fileExtension = ".java";
    this.request.fileFolder = "src";
    sendFileLength();
    sendFile();
    return waitingAnswer();
  }

  @Override
  public void executeAsServer(Request request) {
    this.request.fileExtension = ".java";
    this.request.fileFolder = "server";
    saveFile();
    compileClass();
    instantiateAndSendAnswer(loadClass());
  }

  @Override
  public String toString() {
    return "SOURCEColl";
  }
}
