package banking;

public interface ModelInterface {
    String createAccount();

    boolean logIntoAccount(String cardNumber, String PIN);

    long getAccountBalance();

    void addIncome(long income);

    String receiverCardCheckBeforeTransfer(String receiverCardNumber);

    void doTransfer(String receiverCardNumber, long money) throws NotEnoughMoneyException;

    void closeAccount();

    void logOutOfAccount();
}
