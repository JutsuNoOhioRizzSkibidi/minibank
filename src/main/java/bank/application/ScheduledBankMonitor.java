package bank.application;

import bank.domain.BankAccount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScheduledBankMonitor implements Runnable{
    private final TransferDispatcher dispatcher;
    private final List<BankAccount> accounts;
    private final double expectedTotal;
    public ScheduledBankMonitor(
            TransferDispatcher dispatcher,
            List<BankAccount> accounts,
            double expectedTotal){
        this.dispatcher = dispatcher;
        this.accounts = new ArrayList<>(accounts);
        this.expectedTotal = expectedTotal;

        this.accounts.sort(
                Comparator.comparing(account -> account.getId().value())
        );
    }
    @Override
    public void run(){
        double total = calculateTotal(0);

        boolean invariantOk =
                Double.compare(expectedTotal, total) == 0;
        System.out.println(
                "[MONITOR] поступило: " + dispatcher.getReceived()
                        + ", завершено: " + dispatcher.getCompleted()
                        + ", ожидают: " + dispatcher.getWaiting()
                        + ", баланс: " + total
                        + ", инвариант: " +
                        (invariantOk ? "OK" : "ERROR")
        );
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