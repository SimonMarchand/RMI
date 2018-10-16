import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/***
 * Abstract request helper
 */
public abstract class Request {

  PrintWriter printWriter;
  BufferedReader bufferedReader;
  DataOutputStream dataOutputStream;
  DataInputStream dataInputStream;
  String rawRequest = "";
  public String answer = "";
  public String strategyName = "";
  public StrategyToString strategy;
  String filePath = "";
  String fileFolder = ""; // Put on Client or Server Strategy
  String fileName = "";
  String fileExtension = "";
  String method = "";
  final ArrayList<String> parameters = new ArrayList<>();

  @Override
  public String toString() {
      return strategyName + ' '
          + filePath + ' '
          + fileName + ' '
          + method + ' '
          + parameters.get(0) + ' '
          + parameters.get(1);
  }

  public void initFromRawRequest(){

    StringTokenizer parse = new StringTokenizer(rawRequest);
    strategyName = parse.nextToken();
    filePath= parse.nextToken();
    fileName = parse.nextToken();
    method = parse.nextToken();
    parameters.add(parse.nextToken());
    parameters.add(parse.nextToken());
  }
}