package src;

public class BankAccount {
    private double balance;

    public BankAccount() {
        balance = 0.0;
    }

    public BankAccount(double initialBalance) {
        balance = initialBalance; // creates bank account with set initial balance
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit must be positive.");
            return;
        }
        balance += amount;
        System.out.println("Added " + amount + " to account.");
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }
        if (amount > balance) {
            System.out.println("Not enough money to withdraw " + amount);
            return;
        }
        balance -= amount;
        System.out.println("Withdrew " + amount);
    }

    public void showBalance() {
        System.out.printf("Balance: %.2f\n", balance); // prints balance with two decimal places
    }

    public void transferTo(BankAccount receiver, double amount) {
        if (receiver == null) {
            System.out.println("Target account does not exist.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }
        if (amount > balance) {
            System.out.println("Not enough money to transfer " + amount);
            return;
        }
        this.withdraw(amount);
        receiver.deposit(amount);
        System.out.println("Transferred " + amount);
    }
}
