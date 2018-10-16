import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public abstract class StrategyToString implements Strategy {
  protected final Request request;

  @Override
  public abstract String toString();

  //Const client
  public StrategyToString(Request request) {
    this.request = request;
  }

  /***
   * Initialise the request with the message received from the client
   */
  public void initialiseRequest() {
    try {
      if (this.request.rawRequest != null) {
        //Parsing request
        StringTokenizer parse = new StringTokenizer(this.request.rawRequest);
        System.out.println(this.request.rawRequest);
        this.request.strategyName = parse.nextToken();
        System.out.println("Strategie : " + this.request.strategyName);
        this.request.filePath = parse.nextToken();
        System.out.println("Path : " + this.request.filePath);
        this.request.fileName = parse.nextToken();
        System.out.println("filename : " + this.request.fileName);
        this.request.method = parse.nextToken();
        System.out.println("method : " + this.request.method);

        //get all the parameters
        while (parse.hasMoreTokens()) {
          this.request.parameters.add(parse.nextToken());
        }
      } else {
        System.err.println("Error : The received data is empty.");
      }
    } catch (NoSuchElementException e) {
      System.err.println("Error : " + e.getMessage());
    }
  }


  //Client
  public void sendFileLength() {
    this.request.printWriter.println(
        new File(this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension)
            .length());
    this.request.printWriter.flush();
  }

  public void sendFile() {
    try {
      FileInputStream fis = new FileInputStream(
          this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension);
      byte[] buffer = new byte[4096];
      while (fis.read(buffer) > 0) { //while there is something to read
        if (this.request.dataOutputStream == null) {
          System.out.println("Error : dataOuputStream is null");
        } else {
          this.request.dataOutputStream.write(buffer);
        }
      }
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void serialize(Object object) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(
          this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(object);
      objectOutputStream.close();
      fileOutputStream.close();
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  protected String waitingAnswer() {
    try {
      return this.request.bufferedReader.readLine();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /***
   * Compile the source code file from the request into bytecode
   */
  protected void compileClass() {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    compiler.run(null, null, null,
        this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension);
  }


  protected Object deserialize() {
    Object object = null;
    try {
      FileInputStream fileInputStream = new FileInputStream(
          this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      object = objectInputStream.readObject();
      objectInputStream.close();
      fileInputStream.close();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return object;
  }

  protected void sendAnswer(Class<?> cls, Object object) {
    try {
      Method method = cls.getMethod(this.request.method, String.class, String.class);
      this.request.answer = String.valueOf(method.invoke(object, this.request.parameters.get(0), this.request.parameters.get(1)));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    //Answer
    this.request.printWriter.println(this.request.answer);
    this.request.printWriter.flush();
    System.out.println("Server answer sent : " + this.request.answer);
  }

  protected void instantiateAndSendAnswer(Class<?> loadedClass) {
    Object object;
    if (loadedClass != null) {
      try {
        object = loadedClass.newInstance();
        sendAnswer(loadedClass, object);
      } catch (InstantiationException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  protected void saveFile() {
    try {
      Integer fileSize = Integer.parseInt(this.request.bufferedReader.readLine());
      FileOutputStream fileOutputStream = new FileOutputStream(
          this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName + this.request.fileExtension);
      byte[] buffer = new byte[4096];
      int read;
      int remaining = fileSize;
      while ((read = this.request.dataInputStream.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
        remaining -= read;
        fileOutputStream.write(buffer, 0, read);
      }
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /***
   * Load a class from the file defined in the request
   * @return The loaded class
   */
  protected Class<?> loadClass() {
    try {
      URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{
          new File(this.request.filePath + File.separator + this.request.fileFolder + File.separator + this.request.fileName
              + this.request.fileExtension) //TODO : refactor
              .toURI().toURL()});
      return Class.forName(this.request.fileName, true, classLoader);
    } catch (MalformedURLException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }


}
