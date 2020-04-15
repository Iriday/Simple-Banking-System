package banking;

import java.util.Scanner;

public class ViewConsole implements ViewInterface {
    private ControllerInterface controller;
    private Scanner scn;

    @Override
    public void initialize(ControllerInterface controller) {
        this.controller = controller;
        scn = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {
            output("1. Create account\n2. Log into account\n0. Exit");
            int in;
            try {
                in = Integer.parseInt(scn.nextLine().trim());
            } catch (NumberFormatException e) {
                output("Error, incorrect input\n");
                continue;
            }
            output("");

            if (in == 0) {
                output("Bye!");
                return;
            } else if (in == 1) {
                output(controller.createAccount());
            } else if (in == 2) {
                menuLogIntoAccount();
            } else {
                output("Error, incorrect input");
            }
            output("");
        }
    }

    private void menuLogIntoAccount() {
        output("Enter your card number:");
        String cardNumber = scn.nextLine().trim();
        output("Enter your PIN:");
        String PIN = scn.nextLine().trim();

        boolean loggedIn = controller.logIntoAccount(cardNumber, PIN);
        if (!loggedIn) {
            output("\nWrong card number or PIN!");
            return;
        }
        output("\nYou have successfully logged in!");

        while (true) {
            output("\n1. Balance\n2. Log out\n0. Exit");
            String option = scn.nextLine().trim();
            output("");

            switch (option) {
                case "0" -> {
                    output("Bye!");
                    System.exit(0);
                }
                case "1" -> output("Balance: " + controller.getAccountBalance());
                case "2" -> {
                    controller.logOutOfAccount();
                    output("You have successfully logged out!");
                    return;
                }
                default -> output("Error, incorrect input");
            }
        }
    }

    private <T> void output(T data) {
        System.out.println(data);
    }
}
