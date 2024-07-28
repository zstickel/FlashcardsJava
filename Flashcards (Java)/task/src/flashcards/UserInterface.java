package flashcards;

import java.util.Scanner;

public class UserInterface {
    Scanner scanner;
    CardService cardService;

    UserInterface(CardService cardService, Scanner scanner){
        this.scanner = scanner;
        this.cardService = cardService;
    }
    public void displayMenu(){
        boolean continueProgram = true;
        while (continueProgram) {
            Main.logger.logAndPrint("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String choice = scanner.nextLine();
            Main.logBuffer.add(choice);
            switch (choice){
                case "add":
                    cardService.addCard();
                    break;
                case "remove":
                    cardService.remove();
                    break;
                case "ask":
                    cardService.ask();
                    break;
                case "export":
                    cardService.exportToFile();
                    break;
                case "import":
                    cardService.importFromFile();
                    break;
                case "hardest card":
                    cardService.printHardestCard();
                    break;
                case "reset stats":
                    cardService.resetStats();
                    break;
                case "log":
                    cardService.exportLogs();
                    break;
                case "exit":
                    continueProgram = false;
                    Main.logger.logAndPrint("Bye bye!");
                    break;
                default:
                    break;
            }
        }
    }

}
