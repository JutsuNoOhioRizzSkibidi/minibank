package bank.adapters;

import bank.ports.NotificationService;

public class PrefixNotificationDecorator implements NotificationService {
    private final NotificationService delegate;
    private final String prefix;
    public PrefixNotificationDecorator(NotificationService delegate, String prefix){
        this.delegate = delegate;
        this.prefix = prefix;
    }
    @Override
    public void notify(String message){
        delegate.notify(prefix + message);
    }
}