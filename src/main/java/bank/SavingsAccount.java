package bank;
public class SavingsAccount extends BankAccount{
    private final double minimumBalance;
    public SavingsAccount(
            String number,
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
    public boolean withdraw(double amount) {
        return withdrawKeepMin(amount, minimumBalance);
    }
}
