package flashcards;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {
    private final String term;
    private final String definition;

    private int mistakes;
    Card(String term, String definition){
        this.definition = definition;
        this.term = term;
        mistakes = 0;
    }
    public void resetMistakes(){
        mistakes = 0;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void incrementMistakes(){
        mistakes++;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card myCard = (Card) o;
        return term.equals(myCard.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + term + '\'' +
                ", value=" + definition +
                '}';
    }
}
