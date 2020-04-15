package banking;

public class Account {
    private final Card card;
    private final String PIN;
    private long balance;

    public Account() {
        card = Card.generateUniqueCard();
        PIN = Utils.randNDigitNum(4);
        balance = 0;
    }

    public Card getCard() {
        return card;
    }

    public String getPIN() {
        return PIN;
    }

    public long getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Your card number:\n" + getCard().getCardNumber() + "\nYour card PIN:\n" + getPIN();
    }
}
