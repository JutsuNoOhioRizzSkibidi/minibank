package bank;

public class TransferService{
    private final CommissionPolicy commissionPolicy;
    private final TransferEventPublisher publisher;
    private final BlockedAccountRegistry blockedAccounts;
    private final TransactionJournal journal;

    public TransferService(CommissionPolicy commissionPolicy,
                           NotificationService notificationService,
                           BlockedAccountRegistry blockedAccounts,
                           TransactionJournal journal){
        this(commissionPolicy, new TransferEventPublisher(), blockedAccounts, journal);
        publisher.subscribe(new TransferNotificationListener(notificationService));
    }
    public TransferService(CommissionPolicy commissionPolicy,
                           TransferEventPublisher publisher,
                           BlockedAccountRegistry blockedAccounts,
                           TransactionJournal journal){
        this.commissionPolicy = commissionPolicy;
        this.publisher = publisher;
        this.blockedAccounts = blockedAccounts;
        this.journal = journal;
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
        if (blockedAccounts.isBlocked(from.getId())){
            throw new IllegalStateException("данный счет заблокирован");
        }
        double commission = commissionPolicy.calculate(amount);
        double totalAmount = amount + commission;
        from.withdraw(totalAmount);
        to.deposit(amount);
        Transaction transaction = new Transaction(
                (long) journal.size()+1,
                TransactionType.TRANSFER,
                from.getId(),
                amount,
                TransactionStatus.SUCCESS
        );
        journal.add(transaction);
        publisher.publish(transaction);
    }
}