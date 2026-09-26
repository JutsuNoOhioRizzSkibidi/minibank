package bank;

public class TransferService{
    private final CommissionPolicy commissionPolicy;
    public TransferService(CommissionPolicy commissionPolicy){
        this.commissionPolicy = commissionPolicy;
    }
    public boolean transfer(
            BankAccount from,
            BankAccount to,
            double amount
    )
    {
        if (amount < 0){
            return false;
        }
        if (from == to){
            return false;
        }
        double commission = commissionPolicy.calculate(amount);
        double totalAmount = amount + commission;
        if (!from.withdraw(totalAmount)){
            return false;
        }
        to.deposit(amount);
        return true;
    }
}
