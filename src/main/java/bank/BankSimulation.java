package bank;

import bank.application.*;
import bank.domain.*;
import bank.ports.NotificationService;
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
        AtomicInteger failed = new AtomicInteger();
        NotificationService notification = message -> {};
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        List<Thread> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Thread thread = new Thread(
                    new BankClient(i, accounts, transferService, lock, failed)
            );
            clients.add(thread);
            thread.start();
        }
        for (Thread thread : clients){
            thread.join();
        }
        double finalTotal = 0;
        for (BankAccount account : accounts){
            System.out.println(account.getOwner() + ": " + account.getBalance());
            finalTotal = finalTotal + account.getBalance();
        }
        System.out.println("начанльый баланс: " + initialTotal);
        System.out.println("конечный баланс: " + finalTotal);
        System.out.println("разница: " + (finalTotal - initialTotal));
        System.out.println("инвариант сохранен: " + (initialTotal == finalTotal));
        System.out.println("неуспешных операций: " + failed.get());
    }
}
