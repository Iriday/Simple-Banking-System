package banking;

public interface ModelInterface {
    String createAccount();

    boolean logIntoAccount(String cardNumber, String PIN);

    long getAccountBalance();

    void addIncome(long income);

    void closeAccount();

    void logOutOfAccount();
}
