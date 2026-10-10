package bank;
public class SavingsAccount extends BankAccount{
    private final double minimumBalance;
    public SavingsAccount(
            AccountNumber number,
            String owner,
            double initialBalance,
            double minimumBalance) {
        super (number, owner, initialBalance);

        if (minimumBalance < 0){
            throw new IllegalArgumentException("минимальный остаток не может быть отриц");
        }
        this.minimumBalance = minimumBalance;
    }

    @Override
    public void withdraw(double amount) {
        withdrawKeepMin(amount, minimumBalance);
    }
}
