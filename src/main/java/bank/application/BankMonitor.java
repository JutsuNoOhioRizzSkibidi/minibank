package bank.application;

import bank.domain.BankAccount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BankMonitor implements Runnable{
    private final List<BankAccount> accounts;
    private final double expectedTotal;
    public BankMonitor(List<BankAccount> accounts, double expectedTotal){
        this.accounts = new ArrayList<>(accounts);
        this.accounts.sort(
                Comparator.comparing(account -> account.getId().value())
        );
        this.expectedTotal = expectedTotal;
    }
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            double total = calculateTotal(0);
            if (total == expectedTotal){
                System.out.println("[MONITOR] total=" + total + " OK");
            }
            else{
                System.out.println("[MONITOR] total=" + total + " ERROR");
            }
            try{
                Thread.sleep(20);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[MONITOR] работа завершена");
    }
    private double calculateTotal(int index){
        if (index == accounts.size()){
            double total = 0;
            for (BankAccount account : accounts){
                total = total + account.getBalance();
            }
            return total;
        }
        synchronized (accounts.get(index)){
            return calculateTotal(index + 1);
        }
    }
}
