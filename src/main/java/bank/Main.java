package bank;
public class Main {
    public static void main(String[] args) {
        Transaction deposit = new Transaction(
                1L,
                TransactionType.DEPOSIT,
                new AccountNumber("1234567890"),
                5000,
                TransactionStatus.SUCCESS
        );
        Transaction withdrawal = new Transaction(
                2L,
                TransactionType.WITHDRAWAL,
                new AccountNumber("1234567890"),
                10000,
                TransactionStatus.REJECTED
        );
        Transaction transfer = new Transaction(
                3L,
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