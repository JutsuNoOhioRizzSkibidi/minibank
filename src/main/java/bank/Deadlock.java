package bank;

import bank.application.TransferService;
import bank.domain.BankAccount;

public class Deadlock {
    public static void run(
            TransferService transferService,
            BankAccount accountA,
            BankAccount accountB
    ) throws InterruptedException{
        Runnable firstClient = () -> {
            for (int i = 0; i < 100000; i++){
                try{
                    transferService.transfer(accountA, accountB, 1);
                }
                catch (RuntimeException e){
                    System.out.println("ошибка первого клиента: " + e.getMessage());
                }
            }
        };
        Runnable secondClient = () -> {
            for (int i = 0; i < 100000; i++){
                try{
                    transferService.transfer(accountB, accountA, 1);
                }
                catch (RuntimeException e){
                    System.out.println("ошибка второго клиента: " + e.getMessage());
                }
            }
        };
        Thread thread1 = new Thread(firstClient, "Thread 1");
        Thread thread2 = new Thread(secondClient, "Thread 2");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("оба потока завершились");
        System.out.println("баланс A: " + accountA.getBalance());
        System.out.println("баланс B: " + accountB.getBalance());
        System.out.println("общая сумма: "
                + (accountA.getBalance() + accountB.getBalance()));
    }
}
