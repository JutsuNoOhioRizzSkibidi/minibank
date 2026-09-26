package bank;

public abstract class BankAccount{
    private final String number;
    private final String owner;
    private double balance;

    protected BankAccount(
            String number,
            String owner,
            double initialBalance){
        if (initialBalance < 0) {
            throw new IllegalArgumentException("начальный баланс не может быть отрицательным");
        }
            this.number = number;
            this.owner = owner;
            this.balance = initialBalance;
    }
    public void deposit(double amount){
        if (amount > 0){
            balance = balance + amount;
        }
    }
    public abstract boolean withdraw(double amount);
    public double getBalance() {
        return balance;
    }
    protected boolean withdrawKeepMin(
            double amount,
            double minimumBalance){
        if (amount <= 0){
            return false;
        }
        if (balance - amount < minimumBalance){
            return false;
        }
        balance = balance - amount;
        return true;
    }
}
