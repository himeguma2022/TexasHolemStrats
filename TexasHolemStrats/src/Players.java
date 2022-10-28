import java.util.ArrayList;

public class Players {
    private String name;
    private int money;
    private ArrayList<Card> hand;
    private PlayerAI strats;
    private boolean blind;
    private boolean fold;
    private int moneyInPot;
    private boolean allIn;
    private int callInMin;
    private int handPower;
    private int AIMode;

    public int getAIMode() {
        return AIMode;
    }

    public Players() {
        name = "Takumi";
        money = 200;
        hand = new ArrayList<Card>(2);
        blind = false;
        moneyInPot = 0;
        allIn = false;
        AIMode = 1;
    }

    public Players(String name) {
        this.name = name;
        money = 200;
        hand = new ArrayList<Card>(2);
        blind = false;
        moneyInPot = 0;
        allIn = false;
        AIMode = 1;
    }

    public Players(String name, int buyIn) {
        this.name = name;
        money = buyIn;
        hand = new ArrayList<Card>(2);
        blind = false;
        moneyInPot = 0;
        allIn = false;
        AIMode = 1;
    }

    public Players(String name, int buyIn, int AIMode) {
        this.name = name;
        money = buyIn;
        hand = new ArrayList<Card>(2);
        blind = false;
        moneyInPot = 0;
        allIn = false;
        this.AIMode = AIMode;
    }

    public int Call(int amount) {
        if (money > amount) {
            money = money - amount;
            moneyInPot = moneyInPot + amount;
            return amount;
        }
        int temp = money;
        money = 0;
        allIn = true;
        return temp;
    }

    public void Win(int amount) {
        money = money + amount;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void addToHand(Card inserting) {
        hand.add(inserting);
    }

    public int PreFlop(int minAmount) {
        strats.RecordWholeHand(hand);
        strats.RecordBlindState(blind);
        strats.RecordMoney(money);
        strats.RecordMoneyInPot(moneyInPot);
        strats.RecordCallInMin(minAmount);
        strats.RecordFoldState(fold);
        return strats.BetPreFlop();
    }

    public int PostFlopBet(int minAmount) {
        strats.RecordCallInMin(minAmount);
        strats.RecordMoney(money);
        strats.RecordMoneyInPot(moneyInPot);
        strats.RecordFoldState(fold);
        return strats.HandPowerBet();

    }

    public void OnBlind() {
        blind = true;
    }

    public void OffBlind() {
        blind = false;
    }

    public String ShowHand() {
        return hand.get(0).getFullName() + " and " + hand.get(1).getFullName();
    }

    public boolean blindStatus() {
        return blind;
    }

    public boolean foldedState() {
        return fold;
    }

    public int getCallInMin() {
        return callInMin;
    }

    public void setCallInMin(int callInMin) {
        this.callInMin = callInMin;
    }

    public void ActivateAI() {
        strats = new PlayerAI(AIMode);
    }

    public void onFold() {
        fold = true;
    }

    public int getMoneyInPot() {
        return moneyInPot;
    }

    public void raisedMin(int amount) {
        callInMin = amount;
        strats.RecordCallInMin(amount);
    }

    public boolean isAllIn() {
        return allIn;
    }

    public void recordHandRank(int handRank) {
        handPower = handRank;
        strats.RecordHandPower(handPower);
    }

    public void newCardRevealed() {
        strats.resetRaise();
    }

    public int getHandPower() {
        return handPower;
    }

    public ArrayList<Card> nextRound() {
        ActivateAI();
        ArrayList<Card> outHand = new ArrayList<Card>();
        outHand.addAll(hand);
        hand = new ArrayList<Card>(2);
        blind = false;
        moneyInPot = 0;
        allIn = false;
        fold = false;
        return outHand;
    }

}
