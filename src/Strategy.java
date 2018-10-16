public interface Strategy {
  String executeAsClient(Request clientRequest);
  void executeAsServer(Request serverRequest);

}
