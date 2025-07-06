package src;

import java.util.*;
import java.io.*;



public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<BankAccount> accounts = new ArrayList<>();

        while (true) {
            System.out.println("\nChoose an action: create / deposit / withdraw / transfer / balance / export / exit");
            String command = scanner.nextLine().toLowerCase();

            switch (command) {
                case "create":
                    System.out.print("Enter initial balance: ");
                    double init = scanner.nextDouble();
                    BankAccount acc = new BankAccount(init);
                    accounts.add(acc);
                    System.out.println("Account created. ID: " + (accounts.size() - 1));
                    scanner.nextLine(); // consume newline
                    break;

                case "deposit":
                    System.out.print("Enter account ID: ");
                    int depId = scanner.nextInt();
                    if (isValid(depId, accounts)) {
                        System.out.print("Enter amount to deposit: ");
                        double dep = scanner.nextDouble();
                        accounts.get(depId).deposit(dep);
                    }
                    scanner.nextLine();
                    break;

                case "withdraw":
                    System.out.print("Enter account ID: ");
                    int wdId = scanner.nextInt();
                    if (isValid(wdId, accounts)) {
                        System.out.print("Enter amount to withdraw: ");
                        double wd = scanner.nextDouble();
                        accounts.get(wdId).withdraw(wd);
                    }
                    scanner.nextLine();
                    break;

                case "transfer":
                    System.out.print("Enter source account ID: ");
                    int srcId = scanner.nextInt();
                    System.out.print("Enter target account ID: ");
                    int tgtId = scanner.nextInt();
                    if (isValid(srcId, accounts) && isValid(tgtId, accounts)) {
                        System.out.print("Enter amount to transfer: ");
                        double amt = scanner.nextDouble();
                        accounts.get(srcId).transferTo(accounts.get(tgtId), amt);
                    }
                    scanner.nextLine();
                    break;

                case "balance":
                    System.out.print("Enter account ID: ");
                    int bId = scanner.nextInt();
                    if (isValid(bId, accounts)) {
                        accounts.get(bId).showBalance();
                    }
                    scanner.nextLine();
                    break;

                case "export":
                    exportAccounts(accounts);
                    break;

                case "exit":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    private static boolean isValid(int id, List<BankAccount> accounts) {
        if (id >= 0 && id < accounts.size()) { // if account number is not negative and is less than
                                                //quantity of created accounts
            return true;
        }
        System.out.println("Invalid account ID.");
        return false;
    }

    private static void exportAccounts(List<BankAccount> accounts) { // accounts - gets list accounts, which contatins all the bank accounts
        try (PrintWriter writer = new PrintWriter("src/accounts.csv")) { // opens a flow for writing text into the file
            // PrintWriter - class for writing text into the file
            // try - automatic close of the file after filling it
            writer.println("AccountID,Balance");
            for (int i = 0; i < accounts.size(); i++) { // i - account number, accounts.get(i).getBalance() - gets the balance
                writer.printf("%d,%.2f%n", i, accounts.get(i).getBalance());// %d - integer, %.2f - float or double
            }
            System.out.println("Accounts exported to accounts.csv");
        } catch (Exception e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }
}
