package bank;

public class CreditAccount extends BankAccount{
    private final double creditLimit;
    public CreditAccount(
            String number,
            String owner,
            double initialBalance,
            double creditLimit){
        super (number, owner, initialBalance);
        if (creditLimit < 0){
            throw new IllegalArgumentException("Кредитный лимит не может быть отриц");
        }
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        return withdrawKeepMin(amount, -creditLimit);
    }
}
