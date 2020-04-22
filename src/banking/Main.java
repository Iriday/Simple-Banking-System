package banking;

public class Main {
    public static void main(String[] args) {
        ViewInterface view = new ViewConsole();
        ModelInterface model = new Model(args);
        ControllerInterface controller = new Controller(model, view);
    }
}
