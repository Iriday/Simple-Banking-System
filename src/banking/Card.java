package banking;

import java.util.HashSet;
import java.util.Set;

public class Card {
    private static final Set<Card> cards = new HashSet<>();
    private final String cardNumber;

    private Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    // factory method
    public static Card generateUniqueCard() {
        StringBuilder builderCardNum;
        Card newCard;
        while (true) {
            builderCardNum = new StringBuilder();
            builderCardNum.append("400000"); // Issuer Identification Number (IIN)
            builderCardNum.append(Utils.randNDigitNum(9)); // account identifier | customer account number
            builderCardNum.append(Utils.randNDigitNum(1)); // check digit | checksum

            newCard = new Card(builderCardNum.toString());
            if (!cards.contains(newCard)) {
                cards.add(newCard);
                return newCard;
            }
        }
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return cardNumber.equals(((Card) obj).cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }
}
