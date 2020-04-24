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
        menuMain();
    }

    private void menuMain() {
        while (true) {
            output("1. Create account\n2. Log into account\n0. Exit");
            String option = scn.nextLine().trim();
            output("");

            switch (option) {
                case "0" -> {
                    output("Bye!");
                    return;
                }
                case "1" -> output(controller.createAccount());
                case "2" -> menuAccount();
                default -> output("Error, incorrect input");
            }
            output("");
        }
    }

    private void menuAccount() {
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
            output("""
                                        
                    1. Balance
                    2. Close account
                    3. Log out
                    0. Exit""");
            String option = scn.nextLine().trim();
            output("");

            switch (option) {
                case "0" -> {
                    output("Bye!");
                    System.exit(0);
                }
                case "1" -> output("Balance: " + controller.getAccountBalance());
                case "2" -> {
                    controller.closeAccount();
                    output("You have successfully closed account!");
                    return;
                }
                case "3" -> {
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
