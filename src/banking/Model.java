package banking;

import java.util.ArrayList;
import java.util.List;

public class Model implements ModelInterface {
    private final List<Account> accounts = new ArrayList<>();

    @Override
    public String createAccount() {
        Account account = new Account();
        accounts.add(account);
        return "Your card have been created\n" + account.toString();
    }
}
