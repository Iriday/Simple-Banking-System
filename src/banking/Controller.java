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
    public void createAccount() {

    }

    @Override
    public void logIntoAccount() {

    }
}
