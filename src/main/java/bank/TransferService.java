package bank;

public class TransferService{
    private final CommissionPolicy commissionPolicy;
    private final NotificationService notificationService;
    public TransferService(CommissionPolicy commissionPolicy, NotificationService notificationService){
        this.commissionPolicy = commissionPolicy;
        this.notificationService = notificationService;
    }
    public void transfer(
            BankAccount from,
            BankAccount to,
            double amount
    )
    {
        if (amount <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        if (from.equals(to)){
            throw new IllegalArgumentException("нельзя перевести на тот же счет");
        }
        if (amount >50_000){
            throw new TransferLimitExceededException("превышен лимит перевода");
        }
        double commission = commissionPolicy.calculate(amount);
        double totalAmount = amount + commission;
        from.withdraw(totalAmount);
        to.deposit(amount);
        notificationService.notify("перевод " + amount + " выполнен");
    }
}
