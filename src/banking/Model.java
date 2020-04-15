package banking;

import java.util.ArrayList;
import java.util.List;

public class Model implements ModelInterface {
    private final List<Account> accounts = new ArrayList<>();
    private boolean loggedIn = false;
    private Account currentAccount = null;

    @Override
    public String createAccount() {
        Account account = new Account();
        accounts.add(account);
        return "Your card have been created\n" + account.toString();
    }

    @Override
    public boolean logIntoAccount(String cardNumber, String PIN) {
        for (Account acc : accounts) {
            if (acc.getCard().getCardNumber().equals(cardNumber) && acc.getPIN().equals(PIN)) {
                loggedIn = true;
                currentAccount = acc;
                return true;
            }
        }
        return false;
    }

    @Override
    public long getAccountBalance() {
        if (loggedIn) return currentAccount.getBalance();
        else {
            throw new IllegalStateException("User not logged in");
        }
    }

    @Override
    public void logOutOfAccount() {
        loggedIn = false;
        currentAccount = null;
    }
}
