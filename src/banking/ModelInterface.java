package banking;

public interface ModelInterface {
    Card createAccount();

    boolean logIntoAccount(String cardNumber, String PIN);

    long getAccountBalance();

    void addIncome(long income);

    void getMoney(long money) throws NotEnoughMoneyException;

    String receiverCardCheckBeforeTransfer(String receiverCardNumber);

    void doTransfer(String receiverCardNumber, long money) throws NotEnoughMoneyException;

    void closeAccount();

    void logOutOfAccount();
}
