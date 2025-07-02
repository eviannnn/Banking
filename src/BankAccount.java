package src;

public class BankAccount {
    private double balance;

    // Default constructor
    public BankAccount() {
        this.balance = 0.0;
    }

    // Constructor with balance parameter
    public BankAccount(double balance) {
        this.balance = balance;
    }

    // Method to deposit money
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    // Method to withdraw money
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Invalid or insufficient funds.");
        }
    }

    // Print current balance
    public void printBalance() {
        System.out.println("Current balance: " + balance);
    }

    // Transfer to another BankAccount
    public void transfer(BankAccount other, double amount) {
        if (amount > 0 && amount <= balance) {
            this.withdraw(amount);
            other.deposit(amount);
            System.out.println("Transferred " + amount + " to another account.");
        } else {
            System.out.println("Transfer failed. Check amount and balance.");
        }
    }
}
