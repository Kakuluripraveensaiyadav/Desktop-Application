import java.util.Scanner;

class Bank {
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    // Constructor
    public Bank(String accountNumber, String accountHolderName, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Methods
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. Current balance: " + balance);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful. Current balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public void displayBalance() {
        System.out.println("Current balance: " + balance);
    }
}

public class OnlineBankingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank("123456789", "John Doe", 1000.0);

        int choice;
        double amount;

        do {
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Display Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    amount = scanner.nextDouble();
                    bank.deposit(amount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    amount = scanner.nextDouble();
                    bank.withdraw(amount);
                    break;
                case 3:
                    bank.displayBalance();
                    break;
                case 4:
                    System.out.println("Thank you for using our banking system.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
        scanner.close();
    }
}