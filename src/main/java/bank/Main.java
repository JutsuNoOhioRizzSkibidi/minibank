package bank;
public class Main {
    public static void main(String[] args) {
        Transaction deposit = new Transaction(
                TransactionType.DEPOSIT,
                new AccountNumber("1234567890"),
                5000,
                TransactionStatus.SUCCESS
        );
        Transaction withdrawal = new Transaction(
                TransactionType.WITHDRAWAL,
                new AccountNumber("1234567890"),
                10000,
                TransactionStatus.REJECTED
        );
        Transaction transfer = new Transaction(
                TransactionType.TRANSFER,
                new AccountNumber("0987654321"),
                1500,
                TransactionStatus.SUCCESS
        );
        System.out.println(deposit);
        System.out.println(withdrawal);
        System.out.println(transfer);
    }
}