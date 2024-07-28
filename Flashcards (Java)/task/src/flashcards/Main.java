package flashcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<String> logBuffer = new ArrayList<>();
    public static Logger logger = Logger.getLoggerInstance();
    private static boolean cardsOnExit = false;
    private static String cardsOnExitFile;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CardService cardService = handleArgs(args, scanner);
        UserInterface userInterface = new UserInterface(cardService, scanner);
        userInterface.displayMenu();
        if (cardsOnExit){
            cardService.exportToFile(cardsOnExitFile);
        }
        System.out.println("Dump of all output:");
        for(String outputPhrase: logBuffer){
            System.out.println(outputPhrase);
        }
    }
    private static CardService handleArgs(String[] args, Scanner scanner){
        String inputFile = "";
        if (args.length == 0){
            return new CardService(scanner);
        }
        for(int i=0; i< args.length; i+=2){
            if (args[i].equals("-import")){
                inputFile = args[i+1];
            }
            if(args[i].equals("-export")){
                cardsOnExit = true;
                cardsOnExitFile = args[i+1];
            }
        }
        if (!inputFile.isEmpty()){
            return new CardService(scanner, inputFile);
        }else{
            return new CardService(scanner);
        }
    }
}
