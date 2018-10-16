public class ByteColl extends StrategyToString {

  public ByteColl(ClientRequest request) {
    super(request);
  }

  public ByteColl(ServerRequest request) {
    super(request);
    initialiseRequest();
  }

  @Override
  public String executeAsClient() {
    this.request.fileFolder = "src";
    this.request.fileExtension = ".java";
    compileClass();
    this.request.fileExtension = ".class";
    sendFileLength();
    sendFile();
    return waitingAnswer();
  }

  @Override
  public void executeAsServer() {
    this.request.fileExtension = ".class";
    this.request.fileFolder = "server";
    saveFile();
    instantiateAndSendAnswer(loadClass());
  }

  @Override
  public String toString() {
    return "BYTEColl";
  }
}
