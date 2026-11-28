package bank.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    public void submit(TransferRequest request) {
        int number = received.incrementAndGet();
        waiting.incrementAndGet();
        try{
            executor.execute(() -> {
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
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    System.out.println("заявка прервана: " + request.id());
                }
                catch (RuntimeException e){
                    System.out.println("ошибка заявки "
                            + request.id() + ": " + e.getMessage());
                }
                finally{
                    completed.incrementAndGet();
                }
            });
            submitted.incrementAndGet();
            maxWaiting.accumulateAndGet(
                    waiting.get(),
                    Math::max
            );
        }
        catch (RuntimeException e){
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
