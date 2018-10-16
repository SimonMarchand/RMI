import java.util.Scanner;

/***
 * Request helper for Client
 */
public class ClientRequest extends Request {
  private Scanner scanner;

  /***
   * Initialize the request with an UI
   */
  public void initWithAssistance() {
    chooseWorkingFolder();
    chooseClass();
    chooseMethod();
    chooseParameters();
    rawRequest = toString();
  }

  private void chooseWorkingFolder() {
    System.out.println("Choisir le dossier de travail (hierarchie complète).");
    scanner = new Scanner(System.in);
    filePath = scanner.nextLine(); //No security here, need improvement
  }

  private void chooseClass() {
    System.out.println("Choisir la classe à envoyer (nom seulement).");
    scanner = new Scanner(System.in);
    fileName = scanner.nextLine(); //No security here, need improvement
  }

  private void chooseMethod() {
    System.out.println("Choisir la méthode à faire calculer (nom seulement).");
    scanner = new Scanner(System.in);
    method = scanner.nextLine(); //No security here, need improvement
  }

  private void chooseParameters() {
    System.out.println("Entrer les paramètres de la méthodes."); //Add generics to allow any params amount
    scanner = new Scanner(System.in);
    parameters.add(scanner.nextLine());
    parameters.add(scanner.nextLine());
  }
}
