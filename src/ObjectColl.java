public class ObjectColl extends StrategyToString {

  public ObjectColl(ClientRequest request) {
    super(request);
  }

  public ObjectColl(ServerRequest request) {
    super(request);
    initialiseRequest();
  }

  @Override
  public String executeAsClient() {
    this.request.fileFolder = "src";
    this.request.fileExtension = ".obj";
    serialize(new Calc());
    sendFileLength();
    sendFile();
    return waitingAnswer();
  }

  @Override
  public void executeAsServer() {
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
