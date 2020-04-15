package banking;

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
    public String createAccount() {
        return model.createAccount();
    }

    @Override
    public boolean logIntoAccount(String cardNumber, String PIN) {
        return model.logIntoAccount(cardNumber, PIN);
    }

    @Override
    public long getAccountBalance() {
        return model.getAccountBalance();
    }

    @Override
    public void logOutOfAccount() {
        model.logOutOfAccount();
    }
}
