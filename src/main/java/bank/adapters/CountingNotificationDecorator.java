package bank.adapters;

import bank.ports.NotificationService;

public class CountingNotificationDecorator implements NotificationService {
    private final NotificationService delegate;
    private int count;
    public CountingNotificationDecorator(NotificationService delegate){
        this.delegate = delegate;
        this.count = 0;
    }
    @Override
    public void notify(String message){
        delegate.notify(message);
        count++;
    }
    public int getCount(){
        return count;
    }
}