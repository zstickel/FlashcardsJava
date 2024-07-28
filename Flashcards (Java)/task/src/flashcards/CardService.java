package flashcards;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CardService {
    private Scanner scanner;
    private List<Card> cardList = new ArrayList<>();
    public CardService(Scanner scanner){
        this.scanner = scanner;
    }

    public CardService(Scanner scanner, String importFile){
        this.scanner = scanner;
        importFromFile(importFile);
    }


    public void resetStats(){
        for (Card card: cardList){
            card.resetMistakes();
        }
        Main.logger.logAndPrint("Card statistics have been reset.");
    }

    public void exportLogs(){
        Main.logger.logAndPrint("File name:");
        String logFile = scanner.nextLine();
        Main.logBuffer.add(logFile);
        Main.logger.logAndPrint("The log has been saved.");
        Main.logBuffer.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
        Path filePath = Paths.get(logFile);
        try {
            Files.write(filePath, Main.logBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printHardestCard(){
        List<Card> hardestCardList = getHardestCards();
        if (hardestCardList.isEmpty()){
            Main.logger.logAndPrint("There are no cards with errors.");
            return;
        }else{
            if(hardestCardList.size() == 1){
                Main.logger.logAndPrint("The hardest card is \"" + hardestCardList.get(0).getTerm() + "\". You have " + hardestCardList.get(0).getMistakes() + " errors answering it.");
            }else{
                Main.logger.logAndPrint("The hardest cards are " + getHardestTerms(hardestCardList) + ". You have " + getHardestTotalErrors(hardestCardList) + " errors answering them.");
            }
        }
    }

    private String getHardestTerms(List<Card> hardestCardList){
        String hardestCardTerms = "";
        for(Card card: hardestCardList){
            hardestCardTerms = hardestCardTerms + "\"" + card.getTerm() + "\", ";
        }
        return hardestCardTerms.substring(0, hardestCardTerms.length()-2);
    }

    private int getHardestTotalErrors(List<Card> hardestCardList){
        int numErrors = 0;
        for(Card card: hardestCardList){
            numErrors += card.getMistakes();
        }
        return numErrors;
    }
    private List<Card> getHardestCards(){
        List<Card> hardestCardList = new ArrayList<>();
        int hardestErrors = 0;
        for (Card card: cardList){
            if(card.getMistakes() > hardestErrors){
                hardestErrors = card.getMistakes();
            }
        }
        if (hardestErrors != 0) {
            for (Card card : cardList) {
                if (card.getMistakes() == hardestErrors) {
                    hardestCardList.add(card);
                }
            }
        }
        return hardestCardList;
    }

    private boolean isUniqueTermOrDefinition(String termOrDefinition, String typeTermOrDefinition){
        if (typeTermOrDefinition.equals("term")) {
            for (Card card : cardList) {
                    if (card.getTerm().equals(termOrDefinition)) {
                        return false;
                    }
            }
        }else{
            for (Card card : cardList) {
                if (card.getDefinition().equals(termOrDefinition)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void exportToFile(){
        Main.logger.logAndPrint("File name:");
        String fileName = scanner.nextLine();
        Main.logBuffer.add(fileName);
        saveToFile(fileName);
        Main.logger.logAndPrint(cardList.size() + " cards have been saved.");
    }

    public void exportToFile(String fileName){
        Main.logBuffer.add(fileName);
        saveToFile(fileName);
        Main.logger.logAndPrint(cardList.size() + " cards have been saved.");
    }

    private void saveToFile(String fileName){
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(cardList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importFromFile(){
        Main.logger.logAndPrint("File name:");
        String fileName = scanner.nextLine();
        Main.logBuffer.add(fileName);
        List<Card> fileList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            fileList = (List<Card>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Main.logger.logAndPrint("File not found.");
            return;
            //e.printStackTrace();
        }
        Map<String, Card> cardMap = new LinkedHashMap<>();
        for(Card card: cardList){
            cardMap.put(card.getTerm(), card);
        }
        for(Card card: fileList){
            cardMap.put(card.getTerm(), card);
        }

        cardList = new ArrayList<>(cardMap.values());
        Main.logger.logAndPrint(fileList.size() + " cards have been loaded.");
    }

    private void importFromFile(String fileName){
        List<Card> fileList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            fileList = (List<Card>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Main.logger.logAndPrint("File not found.");
            return;
            //e.printStackTrace();
        }
        Map<String, Card> cardMap = new LinkedHashMap<>();
        for(Card card: cardList){
            cardMap.put(card.getTerm(), card);
        }
        for(Card card: fileList){
            cardMap.put(card.getTerm(), card);
        }

        cardList = new ArrayList<>(cardMap.values());
        Main.logger.logAndPrint(fileList.size() + " cards have been loaded.");
    }


    private String generateTermOrDefinition(String selectedTermOrDefinition){
        String termOrDefinition = scanner.nextLine();
        Main.logBuffer.add(termOrDefinition);
        boolean goodTermOrDefinition= false;
        while(!goodTermOrDefinition){
            if (isUniqueTermOrDefinition(termOrDefinition, selectedTermOrDefinition)){
                goodTermOrDefinition = true;
            }else{
                Main.logger.logAndPrint("The " + selectedTermOrDefinition + " \"" + termOrDefinition + "\" already exists. Try again:" );
                termOrDefinition = scanner.nextLine();
                Main.logBuffer.add(termOrDefinition);
            }
        }
        return termOrDefinition;
    }


    public void generateCardList(){
        Main.logger.logAndPrint("Input the number of cards:");
        int numCards = Integer.parseInt(scanner.nextLine());
        for(int i=1; i<= numCards; i++){
            Main.logger.logAndPrint("Card #" + i + ":");
            String term = generateTermOrDefinition("term");
            Main.logger.logAndPrint("The definition for card #" + i + ":");
            String definition = generateTermOrDefinition("definition");
            cardList.add(new Card(term, definition));
        }
    }
    public void addCard(){
        Main.logger.logAndPrint("The card:");
        String term = scanner.nextLine();
        Main.logBuffer.add(term);
        if(!isUniqueTermOrDefinition(term, "term")){
            Main.logger.logAndPrint("The card \""+ term + "\" already exists.");
            return;
        }
        Main.logger.logAndPrint("The definition of the card:");
        String definition = scanner.nextLine();
        Main.logBuffer.add(definition);
        if(!isUniqueTermOrDefinition(definition, "definition")){
            Main.logger.logAndPrint("The definition \""+ definition + "\" already exists.");
            return;
        }
        Card card = new Card(term, definition);
        cardList.add(card);
        System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added." );
    }

    public void remove(){
        Main.logger.logAndPrint("Which card?");
        String removeCard = scanner.nextLine();
        Main.logBuffer.add(removeCard);
        removeCard(removeCard);
    }

    public void ask(){
        Main.logger.logAndPrint("How many times to ask?");
        String numAsk = scanner.nextLine();
        Main.logBuffer.add(numAsk);
        int num = Integer.parseInt(numAsk);
        askNumTimes(num);
    }

    private void askNumTimes(int num){
        int totalCards = cardList.size();
        int cardIndex = 0;
        for(int i =0; i<num; i++){
            if (cardIndex >= totalCards){
                cardIndex = 0;
            }
            Card card = cardList.get(cardIndex);
            testUser(card);
            cardIndex++;
        }
    }
    private void testUser(Card card){
        Main.logger.logAndPrint("Print the definition of \"" + card.getTerm() + "\":");
        String answer = scanner.nextLine();
        Main.logBuffer.add(answer);
        if (card.getDefinition().equals(answer)) {
            Main.logger.logAndPrint("Correct!");
        } else {
            if (matchesAnotherDefinition(answer)){
                Main.logger.logAndPrint("Wrong. The right answer is \"" + card.getDefinition() + "\", but your definition is correct for \"" + getMatchedDefinition(answer) + "\"." );
                card.incrementMistakes();
            }else {
                Main.logger.logAndPrint("Wrong. The right answer is \"" + card.getDefinition() + "\".");
                card.incrementMistakes();
            }
        }
    }

    private void removeCard(String term){
        if (isCardInList(term)){
            cardList.removeIf(card -> card.getTerm().equals(term));
            Main.logger.logAndPrint("The card has been removed.");
        }else{
            Main.logger.logAndPrint("Can't remove \"" + term + "\": there is no such card.");
        }

    }

    private boolean isCardInList(String term){
        for (Card card: cardList){
            if (term.equals(card.getTerm())){
                return true;
            }
        }
        return false;
    }

    public void printCard(Card card, int number){
        Main.logger.logAndPrint("Card #" + number + ":");
        Main.logger.logAndPrint(card.getTerm());
        Main.logger.logAndPrint("The definition for card #" + number + ":");
        Main.logger.logAndPrint(card.getDefinition());
    }

   private boolean matchesAnotherDefinition(String definition){
        for (Card card: cardList){
            if (definition.equals(card.getDefinition())){
                return true;
            }
        }
        return false;
    }

    private String getMatchedDefinition(String definition){
        for (Card card: cardList){
            if (definition.equals(card.getDefinition())) {
                return card.getTerm();
            }
        }
        return null;
    }

    public void userPractice(){
        for(Card card : cardList) {
            Main.logger.logAndPrint("Print the definition of \"" + card.getTerm() + "\":");
            String answer = scanner.nextLine();
            Main.logBuffer.add(answer);
            if (card.getDefinition().equals(answer)) {
                Main.logger.logAndPrint("Correct!");
            } else {
                if (matchesAnotherDefinition(answer)){
                    Main.logger.logAndPrint("Wrong. The right answer is \"" + card.getDefinition() + "\", but your definition is correct for \"" + getMatchedDefinition(answer) + "\"." );
                }else {
                    Main.logger.logAndPrint("Wrong. The right answer is \"" + card.getDefinition() + "\".");
                }
            }
        }
    }
}
