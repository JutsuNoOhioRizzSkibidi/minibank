package bank;

public class Demo {
    public static void main(String[] args) {
        BankAccount from = new DebitAccount(new AccountNumber("1234567890"), "Vanya", 1000);
        BankAccount to = new DebitAccount(new AccountNumber("1234567891"), "Valya", 2000);
        TransferService transferService =
                new TransferService(
                        new NoCommission(),
                        new ConsoleNotificationService(),
                        new BlockedAccountRegistry());
        try {
            transferService.transfer(from, to, 5000);
            System.out.println("перевод завершён");
        }catch(InsufficientFundsException e){
            System.out.println(
                    "перевод не выполнен: " + e.getMessage()
            );
        }
        System.out.println("баланс отправителя: " + from.getBalance());
        System.out.println("юаланс получателя: " + to.getBalance());
    }
}
