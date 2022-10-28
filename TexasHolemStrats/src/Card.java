import java.util.ArrayList;
import java.util.Stack;

public class Card {
    private int suitID;
    private int number;

    public int getNumber() {
        return number;
    }

    private String symbol;
    private String suit;
    private String fullName;

    public static void main(String[] args) {
        for (int i = 0; i < 52; ++i) {
            Card demo = new Card(i, i);
            System.out.println(demo.fullName);
        }
    }

    public Card(int suitID, int number) {
        this.suitID = suitID % 4;
        switch (suitID % 4) {
            case 0:
                suit = "clubs";
                break;
            case 1:
                suit = "diamonds";
                break;
            case 2:
                suit = "hearts";
                break;
            case 3:
                suit = "spades";
                break;
        }
        this.number = number % 13;
        switch (number % 13) {
            case 1:
                this.number = 14;
                symbol = "Ace";
                break;
            case 2:
                symbol = "Two";
                break;
            case 3:
                symbol = "Three";
                break;
            case 4:
                symbol = "Four";
                break;
            case 5:
                symbol = "Five";
                break;
            case 6:
                symbol = "Six";
                break;
            case 7:
                symbol = "Seven";
                break;
            case 8:
                symbol = "Eight";
                break;
            case 9:
                symbol = "Nine";
                break;
            case 10:
                symbol = "Ten";
                break;
            case 11:
                symbol = "Jack";
                break;
            case 12:
                symbol = "Queen";
                break;
            case 0:
                this.number = 13;
                symbol = "King";
                break;
        }
        fullName = symbol + " of " + suit;
    }

    public boolean SameSuit(Card comparedTo) {
        if (suitID == comparedTo.suitID) {
            return true;
        }
        return false;
    }

    public boolean SameNumber(Card comparedTo) {
        if (number == comparedTo.number) {
            return true;
        }
        return false;
    }

    public int NumberDifference(Card comparedTo) {
        if (number > comparedTo.number) {
            return number - comparedTo.number;
        }
        if (comparedTo.number > number) {
            return comparedTo.number - number;
        }
        return 0;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean SameNumberTriple(Card[] pair) {
        if (SameNumber(pair[0]) && SameNumber(pair[1])) {
            return true;
        }
        return false;
    }

    public boolean SameNumberQuad(Card[] triple) {
        Card[] p1 = { triple[0], triple[1] };
        Card[] p2 = { triple[1], triple[2] };
        if (SameNumberTriple(p1) && SameNumberTriple(p2)) {
            return true;
        }
        return false;
    }

    public boolean isFlush(Stack<Card> check) {
        if (check.isEmpty()) {
            return true;
        }
        if (!SameSuit(check.peek())) {
            return false;
        }
        return check.pop().isFlush(check);
    }

    public boolean isStraight(Stack<Card> check) {
        if (check.isEmpty()) {
            return true;
        }
        if (NumberDifference(check.peek()) != 1) {
            return false;
        }
        return check.pop().isStraight(check);
    }

    public boolean isStrightFlush(Stack<Card> check) {
        return isStraight(check) && isFlush(check);
    }

    public boolean isRoyalFlush(Stack<Card> check) {
        if (number == 14 && isStrightFlush(check)) {
            return true;
        }
        return false;
    }

}
