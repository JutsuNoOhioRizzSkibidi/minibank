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
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
//                amount -> {
//                    if (amount == 123) {
//                        throw new IllegalStateException("тестовая техническая ошибка");
//                    }
//                    return 0;
//                },
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
//            if (i == 2){
//                amount = 123;
//            }
            if (i % 100 == 0){
                amount = -100;
            }
            if (i % 100 == 1){
                to = new AccountNumber("9999999999");
            }
            requests.add(new TransferRequest(from, to, amount));
        }
        TransferDispatcher dispatcher;
        Map<Long, Future<TransferResult>> futures =
                new LinkedHashMap<>();
        List<TransferResult> results;
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
                    Future<TransferResult> future =
                            dispatcher.submit(request);
                    futures.put(request.id(), future);
                    if (dispatcher.getSubmitted() % 250 == 0){
                        System.out.println("отправлено: "
                                + dispatcher.getSubmitted()
                                + ", ожидают: "
                                + dispatcher.getWaiting());
                    }
                }
                results = collectResults(futures);
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
        int successful = 0;
        int rejected = 0;
        int errors = 0;
        for (TransferResult result : results){
            switch (result.status()){
                case SUCCESS -> successful++;
                case REJECTED -> {
                    rejected++;
                    System.out.println("заявка " + result.requestId()
                            + " отклонена: " + result.message());
                }
                case ERROR -> {
                    errors++;
                    System.out.println("техническая ошибка заявки "
                            + result.requestId() + ": " + result.message());
                }
            }
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
        System.out.println("успешных переводов: " + successful);
        System.out.println("бизнес-отказов: " + rejected);
        System.out.println("технических ошибок: " + errors);
        System.out.println("получено результатов: " + results.size());
        System.out.println("все результаты получены: " + (results.size() == dispatcher.getSubmitted()
                && successful + rejected + errors == results.size()));
    }
    private static List<TransferResult> collectResults(
            Map<Long, Future<TransferResult>> futures){
        List<TransferResult> results = new ArrayList<>();
        boolean interrupted = false;
        try{
            for (Map.Entry<Long, Future<TransferResult>> entry
                    : futures.entrySet()){
                long id = entry.getKey();
                Future<TransferResult> future = entry.getValue();
                while (true){
                    try{
                        TransferResult result =
                                future.get(1, TimeUnit.SECONDS);

                        results.add(result);
                        break;
                    }
                    catch (ExecutionException e){
                        results.add(new TransferResult(
                                id,
                                TransferStatus.ERROR,
                                e.getCause().toString()
                        ));
                        break;
                    }
                    catch (TimeoutException e){
                        System.out.println("заявка " + id
                                + ": время ожидания истекло");
                    }
                    catch (InterruptedException e){
                        interrupted = true;
                        System.out.println("ожидание результата прервано");
                    }
                }
            }
        }
        finally{
            if (interrupted){
                Thread.currentThread().interrupt();
            }
        }
        return results;
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
