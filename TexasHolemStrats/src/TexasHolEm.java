import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;

public class TexasHolEm {
    private Stack<Card> deck;
    private Stack<Card> discard;
    private Players[] cast;
    private ArrayList<Card> table;
    private int pot;
    private int bigBlind;
    private int smallBlind;
    private int callInMin;
    private int roundCount;
    private int maxRoundCount;
    private boolean DEBUG;

    public static void main(String[] args) {
        TexasHolEm demo = new TexasHolEm();
        if (demo.DEBUG == false) {
            long start = System.currentTimeMillis();
            int simulating = 100;
            int sum = 0;
            int TakumiLossCount = 0;
            for (int i = 0; i < simulating; ++i) {
                demo = new TexasHolEm();
                sum = sum + demo.roundCount;
                if (demo.cast[0].getMoney() == 0) {
                    ++TakumiLossCount;
                }
            }
            System.out.println("Average number of turns per game: " + sum / simulating);
            System.out.println("Different AI lost: " + TakumiLossCount + " games.");
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Time taken for simulation: " + (timeElapsed / 1000) + " seconds.");
            if (timeElapsed > 60 * 1000) {
                System.out.println("Or, around " + timeElapsed / (60 * 1000) + " minutes.");
            }
        }

    }

    public TexasHolEm() {
        DEBUG = true;
        roundCount = 0;
        pot = 0;
        bigBlind = 10;
        smallBlind = bigBlind / 2;
        maxRoundCount = 1000;
        deck = new Stack<Card>();
        discard = new Stack<Card>();
        for (int i = 0; i < 52; ++i) {
            deck.push(new Card(i, i));
        }
        shuffle(10);
        int buyIn = 1000;
        cast = new Players[10];
        int AIModes = 8;
        cast[0] = new Players("Takumi", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[0].getName() + " using AI Mode: " + cast[0].getAIMode());
        cast[1] = new Players("Miku", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[1].getName() + " using AI Mode: " + cast[1].getAIMode());
        cast[2] = new Players("Yuki", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[2].getName() + " using AI Mode: " + cast[2].getAIMode());
        cast[3] = new Players("Tomoe", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[3].getName() + " using AI Mode: " + cast[3].getAIMode());
        cast[4] = new Players("Shouko", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[4].getName() + " using AI Mode: " + cast[4].getAIMode());
        cast[5] = new Players("Tohru", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[5].getName() + " using AI Mode: " + cast[5].getAIMode());
        cast[6] = new Players("Kyo", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[6].getName() + " using AI Mode: " + cast[6].getAIMode());
        cast[7] = new Players("Anya", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[7].getName() + " using AI Mode: " + cast[7].getAIMode());
        cast[8] = new Players("Yor", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[8].getName() + " using AI Mode: " + cast[8].getAIMode());
        cast[9] = new Players("Loid", buyIn, (int) (Math.floor(AIModes * Math.random()) + 1));
        System.out.println(cast[9].getName() + " using AI Mode: " + cast[9].getAIMode());
        start();
        if (DEBUG) {
            System.out.println("----------------------------------------");
            System.out.println("Game ended by round: " + roundCount);
            ModGameStats();
        }
    }

    private void ModGameStats() {
        for (int i = 0; i < cast.length; ++i) {
            System.out.println(cast[i].getName() + ":\t" + cast[i].getMoney());
        }
    }

    private void shuffle(int times) {
        Stack<Card> A = new Stack<Card>();
        Stack<Card> B = new Stack<Card>();
        while (!deck.empty()) {
            double determiner = Math.random();
            if (determiner > 0.5) {
                A.push(deck.pop());
            } else {
                B.push(deck.pop());
            }
        }
        while (!A.empty()) {
            deck.push(A.pop());
        }
        while (!B.empty()) {
            deck.push(B.pop());
        }
        if (times != 0) {
            shuffle(--times);
        }
    }

    public void start() {
        table = new ArrayList<Card>();
        int dealer = (int) (Math.floor(Math.random() * (cast.length - 1 + 1))) + 2;
        round(dealer);

    }

    private void round(int dealer) {
        if (dealer > cast.length + 2) {
            dealer = dealer - cast.length;
        }
        deal(dealer);
        for (int i = 0; i < cast.length; ++i) {
            if (DEBUG) {
                System.out.println(cast[i].getName() + " has \t" + cast[i].ShowHand());
            }
        }
        bet(dealer);
        flop(dealer);
        bet(dealer);
        turn(dealer);
        bet(dealer);
        river(dealer);
        bet(dealer);

        ArrayList<Integer> winningPlayer = new ArrayList<Integer>();
        int minPower = 0;
        for (int i = 0; i < cast.length; ++i) {
            if (cast[i].getHandPower() > minPower && !cast[i].foldedState()) {
                winningPlayer = new ArrayList<Integer>();
                winningPlayer.add(i);
                minPower = cast[i].getHandPower();
            } else if (cast[i].getHandPower() == minPower && !cast[i].foldedState()) {
                winningPlayer.add(i);
            }
        }
        for (int i = 0; i < winningPlayer.size(); ++i) {
            cast[winningPlayer.get(i)].Win(pot / winningPlayer.size());
            if (DEBUG) {
                System.out.println(cast[winningPlayer.get(i)].getName() + " won " + pot / winningPlayer.size());
            }
        }
        GameStats();
        ++roundCount;
        prepNextRound();
        if (!zeroedOut() && roundCount < maxRoundCount) {
            Scanner uIn = new Scanner(System.in);
            System.out.println("Next round? (Y/N)");
            String choice = uIn.next();
            if (!choice.equals("N")) {
                round(++dealer);
            }
        }
    }

    private void prepNextRound() {
        pot = 0;
        for (int i = 0; i < cast.length; ++i) {
            ArrayList<Card> collectingBack = cast[i].nextRound();
            while (!collectingBack.isEmpty()) {
                discard.push(collectingBack.remove(0));
            }
        }
        while (!table.isEmpty()) {
            discard.push(table.remove(0));
        }
        while (!discard.isEmpty()) {
            deck.push(discard.pop());
        }
        shuffle(10);
    }

    private void river(int dealer) {
        discard.push(deck.pop());
        table.add(deck.pop());
        if (DEBUG) {
            System.out.println(table.get(4).getFullName() + " has entered the river.");
        }
        for (int j = 0; j < cast.length; ++j) {
            cast[(dealer + j) % cast.length].newCardRevealed();
            ;
        }
    }

    private void turn(int dealer) {
        discard.push(deck.pop());
        table.add(deck.pop());
        if (DEBUG) {
            System.out.println(table.get(3).getFullName() + " has entered the turn.");
        }
        for (int j = 0; j < cast.length; ++j) {
            cast[(dealer + j) % cast.length].newCardRevealed();
        }
    }

    private void GameStats() {
        if (DEBUG) {
            for (int i = 0; i < cast.length; ++i) {
                System.out.println(cast[i].getName() + "\t" + cast[i].getMoney() +
                        "\t" + cast[i].getMoneyInPot() + "\t" + cast[i].ShowHand() + "\nValue this round: "
                        + handRank(cast[i].getHand()));
            }
            System.out.println("Money in pot: " + pot);
        }
    }

    private void flop(int dealer) {
        discard.push(deck.pop());
        for (int i = 0; i < 3; ++i) {
            table.add(deck.pop());
            if (DEBUG) {
                System.out.println(table.get(i).getFullName() + " has entered the flop.");
            }
        }
        for (int j = 0; j < cast.length; ++j) {
            cast[(dealer + j) % cast.length].newCardRevealed();

        }
    }

    public void bet(int dealer) {
        boolean raised = false;
        if (!table.isEmpty()) {
            for (int i = 0; i < cast.length; ++i) {
                cast[i].recordHandRank(handRank(cast[i].getHand()));
            }
        }
        for (int i = 0; i < cast.length; ++i) {
            cast[(dealer + i) % cast.length].setCallInMin(callInMin);
            int decisionKey = 0;

            if (table.isEmpty()) {
                decisionKey = cast[(dealer + i) % cast.length].PreFlop(callInMin);
            } else {
                decisionKey = cast[(dealer + i) % cast.length].PostFlopBet(callInMin);
            }

            if (cast[(dealer + i) % cast.length].foldedState()) {
                if (DEBUG) {
                    System.out.println(cast[(dealer + i) % cast.length].getName() + " has folded.");
                }
            } else if (cast[(dealer + i) % cast.length].isAllIn()) {
                if (DEBUG) {
                    System.out.println(cast[(dealer + i) % cast.length].getName() + " has put all in.");
                }
            } else if (decisionKey == -1) {
                cast[(dealer + i) % cast.length].onFold();
                if (DEBUG) {
                    System.out.println(cast[(dealer + i) % cast.length].getName() + " folds.");
                }
            } else if (decisionKey + cast[(dealer + i) % cast.length].getMoneyInPot() > callInMin) {
                raised = true;
                callInMin = decisionKey + cast[(dealer + i) % cast.length].getMoneyInPot();
                for (int j = 0; j < cast.length; ++j) {
                    cast[(dealer + j) % cast.length].raisedMin(callInMin);
                }
                pot = pot + cast[(dealer + i) % cast.length].Call(decisionKey);
                if (DEBUG) {
                    if (cast[(dealer + i) % cast.length].getMoney() == 0) {
                        System.out.println(cast[(dealer + i) % cast.length].getName() + " puts All in.");
                    } else {
                        System.out.println(cast[(dealer + i) % cast.length].getName() + " raises to " + callInMin);
                    }
                }
            } else if (decisionKey + cast[(dealer + i) % cast.length].getMoneyInPot() == callInMin) {
                pot = pot + cast[(dealer + i) % cast.length].Call(decisionKey);
                if (DEBUG) {
                    if (cast[(dealer + i) % cast.length].getMoney() == 0) {
                        System.out.println(cast[(dealer + i) % cast.length].getName() + " puts All in.");
                    } else {
                        System.out.println(cast[(dealer + i) % cast.length].getName() + " checks.");
                    }
                }
            } else {
                pot = pot + cast[(dealer + i) % cast.length].Call(decisionKey);
                if (DEBUG) {
                    System.out.println(cast[(dealer + i) % cast.length].getName() + " puts All in.");
                }
            }

        }
        if (raised) {
            bet(dealer);
        }

    }

    private void deal(int dealer) {
        int bigBlinder = (dealer - 1) % cast.length;
        int smallBlinder = (dealer - 2) % cast.length;
        pot = pot + cast[bigBlinder].Call(bigBlind);
        cast[bigBlinder].OnBlind();
        pot = pot + cast[smallBlinder].Call(smallBlind);
        cast[smallBlinder].OnBlind();
        callInMin = bigBlind;
        for (int i = 0; i < 2 * cast.length; ++i) {
            cast[(dealer + i) % cast.length].addToHand(deck.pop());
        }
        for (int i = 0; i < cast.length; ++i) {
            cast[(dealer + i) % cast.length].setCallInMin(callInMin);
            cast[(dealer + i) % cast.length].ActivateAI();
        }
    }

    public int handRank(ArrayList<Card> hand) {
        ArrayList<Card> combo = new ArrayList<Card>();
        combo.addAll(hand);
        combo.addAll(table);
        Collections.sort(combo, new Comparator<Card>() {
            @Override
            public int compare(Card A, Card B) {
                if (A.getNumber() < B.getNumber()) {
                    return 1;
                }
                return -1;
            }
        });

        if (combo.size() == 7) {
            return fromSeven(combo);
        }
        if (combo.size() == 6) {
            return fromSix(combo);
        }
        return fromFive(combo);

    }

    private int fromSeven(ArrayList<Card> combo) {
        int outVal = 0;
        ArrayList<ArrayList<Card>> combs = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < 7; ++i) {
            ArrayList<Card> inserting = new ArrayList<Card>();
            inserting.addAll(combo);
            inserting.remove(i);
            combs.add(inserting);
        }
        for (int i = 0; i < combs.size(); ++i) {
            int candidate = fromSix(combs.get(i));
            if (candidate > outVal) {
                outVal = candidate;
            }
        }
        return outVal;
    }

    private int fromSix(ArrayList<Card> combo) {
        int outVal = 0;
        ArrayList<ArrayList<Card>> combs = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < 6; ++i) {
            ArrayList<Card> inserting = new ArrayList<Card>();
            inserting.addAll(combo);
            inserting.remove(i);
            combs.add(inserting);
        }
        for (int i = 0; i < combs.size(); ++i) {
            int candidate = fromFive(combs.get(i));
            if (candidate > outVal) {
                outVal = candidate;
            }
        }
        return outVal;
    }

    private int fromFive(ArrayList<Card> combo) {
        ArrayList<Card> preQuad = new ArrayList<Card>();
        preQuad.addAll(combo);
        preQuad.remove(0);
        Stack<Card> quad = new Stack<Card>();
        quad.addAll(preQuad);
        if (combo.get(0).isRoyalFlush(quad)) {
            return 900;
        }
        if (combo.get(0).isStrightFlush(quad)) {
            return combo.get(0).getNumber() + 800;
        }
        for (int i = 3; i < combo.size(); ++i) {
            Card[] triple = { combo.get(i - 3), combo.get(i - 2), combo.get(i - 1) };
            if (combo.get(i).SameNumberQuad(triple)) {
                return combo.get(i).getNumber() + 700;
            }
        }
        Card[] pair = { combo.get(3), combo.get(4) };
        if (combo.get(0).SameNumber(combo.get(1)) && combo.get(2).SameNumberTriple(pair)) {
            return combo.get(0).getNumber() + 600;
        }
        Card[] pair2 = { combo.get(1), combo.get(2) };
        if (combo.get(3).SameNumber(combo.get(4)) && combo.get(0).SameNumberTriple(pair2)) {
            return combo.get(0).getNumber() + 600;
        }
        quad = new Stack<Card>();
        quad.addAll(preQuad);
        if (combo.get(0).isFlush(quad)) {
            return combo.get(0).getNumber() + 500;
        }
        quad = new Stack<Card>();
        quad = FlipStack(preQuad);
        if (combo.get(0).isStraight(quad)) {
            return combo.get(0).getNumber() + 400;
        }
        for (int i = 2; i < combo.size(); ++i) {
            Card[] pair3 = { combo.get(i - 2), combo.get(i - 1) };
            if (combo.get(i).SameNumberTriple(pair3)) {
                return combo.get(i).getNumber() + 300;
            }
        }
        for (int i = 3; i < combo.size(); ++i) {
            if (combo.get(i - 3).SameNumber(combo.get(i - 2)) && combo.get(i - 1).SameNumber(combo.get(i))) {
                return combo.get(i - 3).getNumber() + 200;
            }
        }
        if (combo.get(0).SameNumber(combo.get(1)) && combo.get(3).SameNumber(combo.get(4))) {
            return combo.get(0).getNumber() + 200;
        }
        for (int i = 1; i < combo.size(); ++i) {
            if (combo.get(i - 1).SameNumber(combo.get(i))) {
                return combo.get(i).getNumber() + 100;
            }
        }
        return combo.get(0).getNumber();
    }

    private Stack<Card> FlipStack(ArrayList<Card> preQuad) {
        Stack<Card> out = new Stack<Card>();
        for (int i = preQuad.size() - 1; i > -1; --i) {
            out.push(preQuad.remove(i));
        }
        return out;
    }

    public boolean zeroedOut() {
        for (int i = 0; i < cast.length; ++i) {
            if (cast[i].getMoney() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean oneChecking() {
        for (int i = 0; i < cast.length; ++i) {
            int notFolded = cast.length;
            if (cast[i].foldedState()) {
                --notFolded;
            }
            if (notFolded == 1) {
                return true;
            }
        }
        return false;

    }
}
