package banking;

import java.util.List;

public class Controller implements ControllerInterface {
    private final ModelInterface model;
    private final ViewInterface view;

    public Controller(ModelInterface model, ViewInterface view) {
        this.model = model;
        this.view = view;
        view.initialize(this);
        run();
    }

    private void run() {
        view.run();
    }

    @Override
    public Card createAccount() {
        return model.createAccount();
    }

    @Override
    public boolean logIntoAccount(String cardNumber, String PIN) {
        return model.logIntoAccount(cardNumber, PIN);
    }

    @Override
    public List<Account> getAllAccounts() {
        return model.getAllAccounts();
    }

    @Override
    public long getAccountBalance() {
        return model.getAccountBalance();
    }

    @Override
    public void addIncome(long income) {
        model.addIncome(income);
    }

    @Override
    public void getMoney(long money) throws NotEnoughMoneyException {
        model.getMoney(money);
    }

    @Override
    public String receiverCardCheckBeforeTransfer(String receiverCardNumber) {
        return model.receiverCardCheckBeforeTransfer(receiverCardNumber);
    }

    @Override
    public void doTransfer(String receiverCardNumber, long money) throws NotEnoughMoneyException {
        model.doTransfer(receiverCardNumber, money);
    }

    @Override
    public void closeAccount() {
        model.closeAccount();
    }

    @Override
    public void logOutOfAccount() {
        model.logOutOfAccount();
    }
}
