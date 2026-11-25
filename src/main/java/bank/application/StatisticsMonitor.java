package bank.application;

import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsMonitor implements Runnable{
    private final AtomicInteger successfulTransfers;
    private final AtomicInteger failedTransfers;
    public StatisticsMonitor(AtomicInteger successfulTransfers,
                             AtomicInteger failedTransfers){
        this.successfulTransfers = successfulTransfers;
        this.failedTransfers = failedTransfers;
    }
    @Override
    public void run(){
        int previousTotal = 0;
        long previousTime = System.nanoTime();
        while (!Thread.currentThread().isInterrupted()){
            try{
                Thread.sleep(100);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
            int success = successfulTransfers.get();
            int failed = failedTransfers.get();
            int currentTotal = success + failed;
            long currentTime = System.nanoTime();
            double seconds = (currentTime - previousTime) / 1000000000.0;
            double operationsPerSecond = 0;
            if (seconds > 0){
                operationsPerSecond = (currentTotal - previousTotal) / seconds;
            }
            System.out.println("[STATISTICS]");
            System.out.println("success = " + success);
            System.out.println("failed = " + failed);
            System.out.printf("operations/sec ~= %.2f%n", operationsPerSecond);
            previousTotal = currentTotal;
            previousTime = currentTime;
        }
        System.out.println("[STATISTICS] работа завершена");
    }
}
