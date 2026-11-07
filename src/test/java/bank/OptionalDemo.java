package bank;

public class OptionalDemo {
    public static void main(String[] args){
        BlockedAccountRegistry blockedAccounts =
                new BlockedAccountRegistry();
        TransferService transferService = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService(),
                blockedAccounts,
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(),
                transferService,
                blockedAccounts
        );
        bank.openAccount(new DebitAccount(
                new AccountNumber("1234567890"), "sanya", 50000
        ));
        bank.openAccount(new DebitAccount(
                new AccountNumber("1234567899"), "sanya2", 150000
        ));
        BankAccount account = bank.findFirstRichAccount(100000).orElseThrow(() ->
                new IllegalStateException("нету таких"));
        System.out.println("найден счет: " + account);
        String label = bank.findFirstRichAccount(100000)
                .map(a -> a.getId().value()).orElse("не найдено");
        System.out.println("номер: " + label);
        String missingLabel = bank.findFirstRichAccount(200000)
                .map(a -> a.getId().value()).orElse("не найдено");
        System.out.println("поиск от 200000: " + missingLabel);
    }
}
