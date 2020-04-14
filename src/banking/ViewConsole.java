package banking;

public class ViewConsole implements ViewInterface {
    private ControllerInterface controller;

    @Override
    public void initialize(ControllerInterface controller) {
        this.controller = controller;
    }
}
