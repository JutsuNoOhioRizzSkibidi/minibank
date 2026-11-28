package bank.application;

import bank.domain.BlockedAccountException;
import bank.domain.InsufficientFundsException;
import bank.domain.TransferLimitExceededException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

public class TransferDispatcher {
    private final ExecutorService executor;
    private final TransferApplicationService transferService;
    private final AtomicInteger received = new AtomicInteger();
    private final AtomicInteger submitted = new AtomicInteger();
    private final AtomicInteger completed = new AtomicInteger();
    private final Set<String> workerThreads = ConcurrentHashMap.newKeySet();
    private final AtomicInteger waiting = new AtomicInteger();
    private final AtomicInteger maxWaiting = new AtomicInteger();

    public TransferDispatcher(
            ExecutorService executor,
            TransferApplicationService transferService){
        this.executor = executor;
        this.transferService = transferService;
    }
    public Future<TransferResult> submit(TransferRequest request){
        int number = received.incrementAndGet();
        waiting.incrementAndGet();
        Callable<TransferResult> task = () -> {
                waiting.decrementAndGet();
                String threadName = Thread.currentThread().getName();
                workerThreads.add(threadName);
                if (number <= 12){
                    System.out.println("заявка " + request.id() + ", поток: " + threadName);
                }
                try{
                    Thread.sleep(2);
                    transferService.transfer(
                            request.from(),
                            request.to(),
                            request.amount()
                    );
                    return new TransferResult(
                            request.id(),
                            TransferStatus.SUCCESS,
                            "перевод выполнен"
                    );
                }
                catch (IllegalArgumentException
                       | InsufficientFundsException
                       | TransferLimitExceededException
                       | BlockedAccountException e){

                    return new TransferResult(
                            request.id(),
                            TransferStatus.REJECTED,
                            e.getMessage()
                    );
                }
                finally{
                    completed.incrementAndGet();
                }
        };
        try{
            Future<TransferResult> future = executor.submit(task);
            submitted.incrementAndGet();
            maxWaiting.accumulateAndGet(
                    waiting.get(),
                    Math::max
            );
            return future;
        }
        catch (RejectedExecutionException e){
            waiting.decrementAndGet();
            throw e;
        }
    }
    public int getReceived(){
        return received.get();
    }
    public int getSubmitted(){
        return submitted.get();
    }
    public int getWorkerCount(){
        return workerThreads.size();
    }
    public int getWaiting(){
        return waiting.get();
    }
    public int getMaxWaiting(){
        return maxWaiting.get();
    }
    public int getCompleted(){
        return completed.get();
    }
}
