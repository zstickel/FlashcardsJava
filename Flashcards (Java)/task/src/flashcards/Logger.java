package flashcards;

import java.util.Scanner;

public class Logger {
    private static Logger loggerInstance;
    private Logger(){

    }
    public static Logger getLoggerInstance(){
        if(loggerInstance == null){
            loggerInstance = new Logger();
        }
        return loggerInstance;
    }

    public void logAndPrint(String stringToPrint){
        Main.logBuffer.add(stringToPrint);
        System.out.println(stringToPrint);
    }

}
