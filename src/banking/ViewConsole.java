package banking;

import java.util.Scanner;

public class ViewConsole implements ViewInterface {
    private ControllerInterface controller;

    @Override
    public void initialize(ControllerInterface controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        var scn = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create account\n2. Log into account\n0. Exit");
            int in;
            try {
                in = Integer.parseInt(scn.next().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error, incorrect input\n");
                continue;
            }

            if (in == 0) {
                break;
            } else if (in == 1) {
                controller.createAccount();
            } else if (in == 2) {
                controller.logIntoAccount();
            } else {
                System.out.println("Error, incorrect input\n");
            }
        }
    }
}
