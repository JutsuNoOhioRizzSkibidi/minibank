package bank;

import bank.adapters.CountingNotificationDecorator;
import bank.adapters.LoggingNotificationDecorator;
import bank.adapters.PrefixNotificationDecorator;
import bank.ports.NotificationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationDecoratorTest{
    @Test
    void loggingNotificationTest(){
        FakeNotification fake = new FakeNotification();
        NotificationService notification = new LoggingNotificationDecorator(fake);
        notification.notify("перевод выполнен");
        assertEquals(1, fake.getNotificationCount());
        assertEquals("перевод выполнен", fake.getLastMessage());
    }
    @Test
    void countingNotificationTest(){
        FakeNotification fake = new FakeNotification();
        CountingNotificationDecorator notification = new CountingNotificationDecorator(fake);
        notification.notify("первое уведомление");
        notification.notify("второе уведомление");
        assertEquals(2, notification.getCount());
        assertEquals(2, fake.getNotificationCount());
    }
    @Test
    void prefixNotificationTest(){
        FakeNotification fake = new FakeNotification();
        NotificationService notification = new PrefixNotificationDecorator(fake, "[BANK] ");
        notification.notify("перевод выполнен");
        assertEquals("[BANK] перевод выполнен", fake.getLastMessage());
    }
    @Test
    void decoratorChainTest(){
        FakeNotification fake = new FakeNotification();
        CountingNotificationDecorator counting = new CountingNotificationDecorator(
                new PrefixNotificationDecorator(fake, "[BANK] ")
        );
        NotificationService notification = new LoggingNotificationDecorator(counting);
        notification.notify("перевод выполнен");
        assertEquals(1, counting.getCount());
        assertEquals(1, fake.getNotificationCount());
        assertEquals("[BANK] перевод выполнен", fake.getLastMessage());
    }
}