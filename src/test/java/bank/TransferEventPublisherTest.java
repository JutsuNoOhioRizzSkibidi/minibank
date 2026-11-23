
package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferEventPublisherTest{
    @Test
    void subscribersReceiveEvent(){
        TransferEventPublisher publisher = new TransferEventPublisher();

        TransferCountingListener listener1 = new TransferCountingListener();
        TransferCountingListener listener2 = new TransferCountingListener();

        publisher.subscribe(listener1);
        publisher.subscribe(listener2);

        Transaction transaction = new Transaction(
                1L,
                TransactionType.TRANSFER,
                new AccountNumber("1234567890"),
                1000,
                TransactionStatus.SUCCESS
        );

        publisher.publish(transaction);

        assertEquals(1, listener1.getCount());
        assertEquals(1, listener2.getCount());
    }
    @Test
    void notificationListenerTest(){
        TransferEventPublisher publisher = new TransferEventPublisher();
        FakeNotification fake = new FakeNotification();

        publisher.subscribe(new TransferNotificationListener(fake));

        Transaction transaction = new Transaction(
                1L,
                TransactionType.TRANSFER,
                new AccountNumber("1234567890"),
                3000,
                TransactionStatus.SUCCESS
        );

        publisher.publish(transaction);

        assertEquals(1, fake.getNotificationCount());
        assertEquals("перевод 3000.0 выполнен", fake.getLastMessage());
    }
    @Test
    void noSubscribersTest(){
        TransferEventPublisher publisher = new TransferEventPublisher();

        Transaction transaction = new Transaction(
                1L,
                TransactionType.TRANSFER,
                new AccountNumber("1234567890"),
                1000,
                TransactionStatus.SUCCESS
        );
        assertDoesNotThrow(() -> publisher.publish(transaction));
    }
}