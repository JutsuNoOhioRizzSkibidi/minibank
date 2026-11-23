package bank.adapters;

import bank.ports.NotificationService;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void notify(String messege){
        System.out.println(messege);
    }
}
