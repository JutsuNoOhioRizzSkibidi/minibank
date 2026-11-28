package bank;

import bank.adapters.InMemoryRepository;
import bank.application.*;
import bank.domain.*;
import bank.ports.NotificationService;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankPoolSimulation {
    public static void main(String[] args){
        List<BankAccount> accounts = List.of(
                new DebitAccount(new AccountNumber("1234567890"), "sanya", 100000),
                new DebitAccount(new AccountNumber("1234567891"), "sanya1", 100000),
                new DebitAccount(new AccountNumber("1234567892"), "sanya2", 100000),
                new DebitAccount(new AccountNumber("1234567893"), "sanya3", 100000),
                new DebitAccount(new AccountNumber("1234567894"), "sanya4", 100000)
        );

        InMemoryRepository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        double initialTotal = 0;
        for (BankAccount account : accounts){
            repository.save(account);
            initialTotal = initialTotal + account.getBalance();
        }
        NotificationService notification = message -> {};
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification, new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        TransferApplicationService applicationService = new TransferApplicationService(repository, transferService);
        TransferDispatcher dispatcher;
        try (ExecutorService executor = Executors.newFixedThreadPool(4)){
            dispatcher = new TransferDispatcher(
                    executor, applicationService
            );
            for (int i = 1; i <= 1000; i++){
                int fromIndex = i % accounts.size();
                int toIndex = (fromIndex + 1) % accounts.size();
                AccountNumber from = accounts.get(fromIndex).getId();
                AccountNumber to = accounts.get(toIndex).getId();
                double amount = 100;
                if (i % 100 == 0){
                    amount = -100;
                }
                if (i % 100 == 1){
                    to = new AccountNumber("9999999999");
                }
                TransferRequest request = new TransferRequest(
                        from,
                        to,
                        amount
                );
                dispatcher.submit(request);


            }
        }
        double finalTotal = 0;
        for(BankAccount account : accounts){
            finalTotal = finalTotal + account.getBalance();
        }
        System.out.println("=== Итоговый отчет ===");
        System.out.println("поступило заявок: " + dispatcher.getReceived());
        System.out.println("передано исполнителю: " + dispatcher.getSubmitted());
        System.out.println("начальный баланс: " + initialTotal);
        System.out.println("конечный баланс: " + finalTotal);
        System.out.println("инвариант сохранен: " + (initialTotal == finalTotal));
        System.out.println("все потоки завершены");
    }
}
