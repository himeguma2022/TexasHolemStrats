import java.util.ArrayList;
import java.util.Scanner;

public class PlayerAI {
    private ArrayList<Card> hand;
    private ArrayList<Card> FiveCard;
    private boolean blind;
    private boolean fold;
    private int moneyInPot;
    private int money;
    private int callInMin;
    private boolean raise;
    private int handPower;
    private double bluffing;
    private int AIMode;
    private int betConstant;
    private Scanner uIn;
    private double raiseAgain;

    public PlayerAI() {
        hand = new ArrayList<Card>(2);
        FiveCard = new ArrayList<Card>(5);
        blind = false;
        raise = false;
        fold = false;
        bluffing = Math.random();
        raiseAgain = 0;
        AIMode = 2;
        betConstant = 90;
    }

    public PlayerAI(int AIMode) {
        hand = new ArrayList<Card>(2);
        FiveCard = new ArrayList<Card>(5);
        blind = false;
        raise = false;
        fold = false;
        betConstant = 90;
        this.AIMode = AIMode;
        switch (AIMode) {
            case 0:
                uIn = new Scanner(System.in);
                bluffing = 0;
                raiseAgain = 0;
                break;
            case 1:
                bluffing = 0;
                raiseAgain = 0;
                break;
            case 2:
                bluffing = Math.random();
                raiseAgain = 0;
                break;
            case 3:
                bluffing = 0.5 + (0.5 * Math.random());
                raiseAgain = 0;
                break;

            case 4:
                bluffing = 0.6 + (0.4 * Math.random());
                raiseAgain = 0;
                break;
            case 5:
                bluffing = 0;
                raiseAgain = Math.random();
                break;
            case 6:
                bluffing = Math.random();
                raiseAgain = Math.random();
                break;
            case 7:
                bluffing = 0.5 + (0.5 * Math.random());
                raiseAgain = Math.random();
                break;

            case 8:
                bluffing = 0.6 + (0.4 * Math.random());
                raiseAgain = Math.random();
                break;

        }
    }

    public void RecordInHand(Card input) {
        hand.add(input);
    }

    public void RecordWholeHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void RecordInFiveCard(Card input) {
        FiveCard.add(input);
    }

    public int BetPreFlop() {
        if (AIMode == 0) {
            if (fold) {
                return -1;
            }
            if (money == 0) {
                return money;
            }
            System.out.println(
                    "-1 to fold, call in minimum is " + (callInMin - moneyInPot) + " u have this much left: " + money);
            int callTo = uIn.nextInt();
            if (callTo == -1) {
                return -1;
            }
            if (callTo < callInMin - moneyInPot) {
                return BetPreFlop();
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.999) {
            return money;
        }
        if (bluffing > 0.9 && !raise) {
            raise = true;
            int callTo = callInMin - moneyInPot + (money / (4 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.75 && !raise) {
            raise = true;
            int callTo = callInMin - moneyInPot + (money / (6 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (hand.get(0).SameNumber(hand.get(1)) && !raise) {
            raise = true;
            int callTo = callInMin - moneyInPot + (money / (6 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }

        if (!StartFold() || bluffing > 0.6) {
            int callTo = callInMin - moneyInPot;
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        return -1;

    }

    public boolean StartFold() {
        if (blind) {
            return false;
        }
        if (hand.get(0).SameSuit(hand.get(1))) {
            return false;
        }
        if (hand.get(0).SameNumber(hand.get(1))) {
            return false;
        }
        if (hand.get(0).NumberDifference(hand.get(1)) < 5) {
            return false;
        }
        return true;
    }

    public void RecordBlindState(boolean blind) {
        this.blind = blind;
    }

    public void RecordFoldState(boolean fold) {
        this.fold = fold;
    }

    public boolean Folded() {
        return fold;
    }

    public void RecordMoneyInPot(int moneyInPot) {
        this.moneyInPot = moneyInPot;
    }

    public void RecordMoney(int money) {
        this.money = money;
    }

    public void RecordCallInMin(int amount) {
        callInMin = amount;
    }

    public void RecordHandPower(int handPower) {
        this.handPower = handPower;
    }

    public int HandPowerBet() {

        int callTo = 0;
        if (AIMode == 0) {
            if (fold) {
                return -1;
            }
            if (money == 0) {
                return money;
            }
            System.out.println(
                    "-1 to fold, call in minimum is " + (callInMin - moneyInPot) + " u have this much left: " + money);
            callTo = uIn.nextInt();
            if (callTo == -1) {
                return -1;
            }
            if (callTo < callInMin - moneyInPot) {
                return BetPreFlop();
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.999) {
            return money;
        }
        if (bluffing > 0.99 && !raise) {
            raise = true;
            callTo = callInMin - moneyInPot + (9 * money / (2 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.97 && !raise) {
            raise = true;
            callTo = callInMin - moneyInPot + (9 * money / (3 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.85 && !raise) {
            raise = true;
            callTo = callInMin - moneyInPot + ((1 + (handPower / 100)) * money / (2 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.8 && !raise) {
            raise = true;
            callTo = callInMin - moneyInPot + ((1 + (handPower / 100)) * money / (3 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (bluffing > 0.65 && !raise) {
            raise = true;
            callTo = callInMin - moneyInPot + (((handPower / 100)) * money / (2 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if (!raise) {
            raise = true;
            callTo = callInMin - moneyInPot + ((handPower / 100) * money / (3 * betConstant));
            if (raiseAgain > 0.75) {
                raiseAgain = Math.random();
                raise = false;
            }
            if (callTo > money) {
                return money;
            }
            return callTo;
        }
        if ((900 / 200) * ((callInMin - moneyInPot) / handPower) > 0.95 && bluffing < 0.6) {
            return -1;
        }
        callTo = callInMin - moneyInPot;
        if (callTo > money) {
            return money;
        }
        return callTo;
    }

    public void resetRaise() {
        raise = false;
    }

}
