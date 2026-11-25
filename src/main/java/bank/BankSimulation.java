package bank;

import bank.application.*;
import bank.domain.*;
import bank.ports.NotificationService;
import bank.application.BankMonitor;
import bank.application.StatisticsMonitor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class BankSimulation{
    public static void main(String[] args) throws InterruptedException{
        List<BankAccount> accounts = List.of(
                new DebitAccount(new AccountNumber("1234567890"), "sanya", 100000),
                new DebitAccount(new AccountNumber("1234567891"), "sanya1", 100000),
                new DebitAccount(new AccountNumber("1234567892"), "sanya2", 100000),
                new DebitAccount(new AccountNumber("1234567893"), "sanya3", 100000),
                new DebitAccount(new AccountNumber("1234567894"), "sanya4", 100000)
        );
        double initialTotal = 0;
        for (BankAccount account : accounts){
            initialTotal = initialTotal + account.getBalance();
        }
        System.out.println("начальный баланас " + initialTotal);
        Object lock = new Object();
        AtomicInteger succesfulTransfer = new AtomicInteger();
        AtomicInteger failedTransfer = new AtomicInteger();
        NotificationService notification = message -> {};
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
//        Deadlock.run(
//                transferService,
//                accounts.get(0),
//                accounts.get(1)
//        );
        BankMonitor monitor = new BankMonitor(accounts, initialTotal);
        Thread monitorThread = new Thread(monitor, "bank monitor");
        monitorThread.start();
        StatisticsMonitor statisticsMonitor = new StatisticsMonitor(
                succesfulTransfer, failedTransfer
        );
        Thread statisticsThread = new Thread(
                statisticsMonitor, "Statistics monitor"
        );
        statisticsThread.start();
        List<Thread> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Thread thread = new Thread(
                    new BankClient(i, accounts, transferService, lock, succesfulTransfer, failedTransfer)
            );
            clients.add(thread);
            thread.start();
        }
        for (Thread thread : clients){
            thread.join();
        }
        monitorThread.interrupt();
        statisticsThread.interrupt();
        monitorThread.join();
        statisticsThread.join();
        System.out.println("все потоки завершены");
        int successful = succesfulTransfer.get();
        int failed = failedTransfer.get();
        int totalAttempts = 5 * 1000;
        System.out.println("успешных переводов: " + successful);
        System.out.println("неуспешных переводов: " + failed);
        System.out.println("всего попыток: " + totalAttempts);
        System.out.println("статистика корректна: "
                + (successful + failed == totalAttempts));
        double finalTotal = 0;
        for (BankAccount account : accounts){
            System.out.println(account.getOwner() + ": " + account.getBalance());
            finalTotal = finalTotal + account.getBalance();
        }
        System.out.println("начанльый баланс: " + initialTotal);
        System.out.println("конечный баланс: " + finalTotal);
        System.out.println("разница: " + (finalTotal - initialTotal));
        System.out.println("инвариант сохранен: " + (initialTotal == finalTotal));
    }
}
