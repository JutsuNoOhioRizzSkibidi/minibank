package bank.application;

import bank.domain.AccountNumber;
import java.util.concurrent.atomic.AtomicLong;

public record  TransferRequest(
        long id,
        AccountNumber from,
        AccountNumber to,
        double amount
){
    private static final AtomicLong counter = new AtomicLong();
    public TransferRequest(AccountNumber from,
                           AccountNumber to,
                           double amount){
        this(counter.incrementAndGet(), from, to, amount);
    }
}