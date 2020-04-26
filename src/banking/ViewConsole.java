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
                case "1" -> {
                    Card card = controller.createAccount();
                    output("Your card have been created\n" + "Your card number:\n" + card.getNumber() + "\nYour card PIN:\n" + card.getPin());
                }
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
                    2. Add income
                    3. Get money
                    4. Do transfer
                    5. Close account
                    6. Log out
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
                    output("Enter number: ");
                    try {
                        long number = Long.parseLong(scn.nextLine().trim());
                        if (number < 0) {
                            output("\nIncorrect input, please try again!");
                        } else {
                            controller.addIncome(number);
                            output("\nIncome has been added successfully!");
                        }
                    } catch (NumberFormatException e) {
                        output("\nIncorrect input, please try again!");
                    }
                }
                case "3" -> {
                    output("Enter number:");
                    try {
                        long number = Long.parseLong(scn.nextLine().trim());
                        if (number < 0) {
                            output("\nIncorrect input, please try again!");
                        } else {
                            try {
                                controller.getMoney(number);
                                output("\nOperation has been completed successfully!");
                            } catch (NotEnoughMoneyException e) {
                                output("\nYour balance is less than specified number!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        output("\nIncorrect input, please try again!");
                    }
                }
                case "4" -> {
                    output("Enter receiver card number:");
                    String receiverCardNumber = scn.nextLine().trim();
                    String errMessage = controller.receiverCardCheckBeforeTransfer(receiverCardNumber);
                    if (!errMessage.isEmpty()) {
                        output("\n" + errMessage);
                        continue;
                    }
                    output("Enter number:");
                    try {
                        long number = Long.parseLong(scn.nextLine().trim());
                        if (number < 0) {
                            output("\nIncorrect input, please try again!");
                        } else {
                            try {
                                controller.doTransfer(receiverCardNumber, number);
                                output("\nTransfer has been performed successfully!");
                            } catch (NotEnoughMoneyException e) {
                                output("\nYou do not have enough money to perform this transfer!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        output("\nIncorrect input, please try again!");
                    }
                }
                case "5" -> {
                    controller.closeAccount();
                    output("You have successfully closed account!");
                    return;
                }
                case "6" -> {
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
