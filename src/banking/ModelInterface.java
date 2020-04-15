package banking;

public interface ModelInterface {
    String createAccount();

    boolean logIntoAccount(String cardNumber, String PIN);

    long getAccountBalance();

    void logOutOfAccount();
}
