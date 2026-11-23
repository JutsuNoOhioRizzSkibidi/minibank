package bank;

public class TransferNotificationListener implements TransferListener{
    private final NotificationService notificationService;
    public TransferNotificationListener(NotificationService notificationService){
        this.notificationService = notificationService;
    }
    @Override
    public void onTransferCompleted(Transaction transaction){
        notificationService.notify("перевод " + transaction.amount() + " выполнен");
    }
}