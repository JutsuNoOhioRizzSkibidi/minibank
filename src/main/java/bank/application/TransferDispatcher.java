package bank.application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class TransferDispatcher {
    private final ExecutorService executor;
    private final TransferApplicationService transferService;
    private final AtomicInteger received = new AtomicInteger();
    private final AtomicInteger submitted = new AtomicInteger();

    public TransferDispatcher(
            ExecutorService executor,
            TransferApplicationService transferService){
        this.executor = executor;
        this.transferService = transferService;
    }
    public void submit(TransferRequest request){
        int number = received.incrementAndGet();
        executor.execute(() -> {
            if (number <= 12){
                System.out.println("заявка " + request.id() + ", поток: "
                + Thread.currentThread().getName());
            }
            try{
                transferService.transfer(
                        request.from(),
                        request.to(),
                        request.amount()
                );
            }
            catch (RuntimeException e){
                System.out.println("ошибка заявки " + request.id() + ": " + e.getMessage());
            }
        });
        submitted.incrementAndGet();
    }
    public int getReceived(){
        return received.get();
    }
    public int getSubmitted(){
        return  submitted.get();
    }
}
