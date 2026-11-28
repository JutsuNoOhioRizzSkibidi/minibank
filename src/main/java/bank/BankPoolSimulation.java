package bank;

import bank.adapters.InMemoryRepository;
import bank.application.*;
import bank.domain.*;
import bank.ports.NotificationService;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

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
        String mode = "fixed";
        if (args.length > 0){
            mode = args[0];
        }
        List<TransferRequest> requests = new ArrayList<>();
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
            requests.add(new TransferRequest(from, to, amount));
        }
        TransferDispatcher dispatcher;
        System.out.println("режим работы: " + mode);
        long startTime = System.nanoTime();
        try (ScheduledExecutorService scheduler =
                     Executors.newSingleThreadScheduledExecutor()){
            ScheduledFuture<?> monitorTask = null;
            try (ExecutorService executor = createExecutor(mode)){
                dispatcher = new TransferDispatcher(
                        executor,
                        applicationService
                );
                ScheduledBankMonitor monitor = new ScheduledBankMonitor(
                        dispatcher,
                        accounts,
                        initialTotal
                );
                monitorTask = scheduler.scheduleAtFixedRate(
                        monitor,
                        0,
                        1,
                        TimeUnit.SECONDS
                );
                for (TransferRequest request : requests){
                    dispatcher.submit(request);
                    if (dispatcher.getSubmitted() % 250 == 0){
                        System.out.println("отправлено: "
                                + dispatcher.getSubmitted()
                                + ", ожидают: "
                                + dispatcher.getWaiting());
                    }
                }
            }
            finally{
                if (monitorTask != null){
                    monitorTask.cancel(false);
                }
            }
        }
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000.0;
        double finalTotal = 0;
        for (BankAccount account : accounts){
            finalTotal = finalTotal + account.getBalance();
        }
        System.out.println();
        System.out.println("=== ИТОГОВЫЙ ОТЧЕТ ===");
        System.out.println("режим: " + mode);
        System.out.println("поступило заявок: " + dispatcher.getReceived());
        System.out.println("передано исполнителю: " + dispatcher.getSubmitted());
        System.out.println("завершено заявок: " + dispatcher.getCompleted());
        System.out.println("ожидают обработки: " + dispatcher.getWaiting());
        System.out.println("уникальных потоков: " + dispatcher.getWorkerCount());
        System.out.println("максимум ожидающих задач: " + dispatcher.getMaxWaiting());
        System.out.printf("время выполнения: %.2f мс%n", executionTime);
        System.out.println("начальный баланс: " + initialTotal);
        System.out.println("конечный баланс: " + finalTotal);
        System.out.println("инвариант сохранен: " + (initialTotal == finalTotal));
        System.out.println("все потоки завершены");
    }
    private static ExecutorService createExecutor(String mode){
        return switch (mode){
            case "single" -> Executors.newSingleThreadExecutor();
            case "fixed" -> Executors.newFixedThreadPool(4);
            case "cached" -> Executors.newCachedThreadPool();
            default -> throw new IllegalArgumentException("неизвестный режим");
        };
    }
}
