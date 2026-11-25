package bank.application;

import bank.domain.BankAccount;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BankClient implements Runnable{
    private final int clientId;
    private final List<BankAccount> accounts;
    private final TransferService transferService;
    private final Object lock;
    private final AtomicInteger succesfulTransfers;
    private final AtomicInteger failedTransfers;


    public BankClient(int clientId, List<BankAccount> accounts,
                      TransferService transferService, Object lock,
                      AtomicInteger succesfulTransfers,
                      AtomicInteger failedTransfers){
        this.clientId = clientId;
        this.accounts = accounts;
        this.transferService = transferService;
        this.lock = lock;
        this.succesfulTransfers = succesfulTransfers;
        this.failedTransfers = failedTransfers;
    }
    @Override
    public void run(){
        for (int i = 0; i < 10000; i++){
            if (Thread.currentThread().isInterrupted()){
                break;
            }
            int fromIndex = (clientId + i) % accounts.size();
            int toIndex = (fromIndex + 1 + i % 4) % accounts.size();
            double amount = 100 + (i % 10) * 100;
            BankAccount from = accounts.get(fromIndex);
            BankAccount to = accounts.get(toIndex);
            try{

                    transferService.transfer(from, to, amount);
                    succesfulTransfers.incrementAndGet();

            }
            catch (RuntimeException e){
                failedTransfers.incrementAndGet();
            }
        }
        System.out.println("клиент " + clientId + " завершил работу");
    }
}
