package banking;

public interface ControllerInterface {
    String createAccount();

    boolean logIntoAccount(String cardNumber, String PIN);

    long getAccountBalance();

    void closeAccount();

    void logOutOfAccount();
}
