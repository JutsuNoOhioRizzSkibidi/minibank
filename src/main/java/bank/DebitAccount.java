package bank;

public class DebitAccount extends BankAccount {
    public DebitAccount(
            AccountNumber number,
            String owner,
            double initialBalance ) {
        super(number, owner, initialBalance);
    }
    @Override
    public void withdraw(double amount){
        withdrawKeepMin(amount, 0);
    }
}
