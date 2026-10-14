package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferQueueTest {
    @Test
    void requestsAreRetrievedInInsertionOrder(){
        TransferQueue queue = new TransferQueue();
        AccountNumber from = new AccountNumber("0000000001");
        AccountNumber to = new AccountNumber("0000000002");
        TransferRequest first = new TransferRequest(from, to, 100);
        TransferRequest second = new TransferRequest(from, to, 200);
        TransferRequest third = new TransferRequest(from, to, 300);
        queue.add(first);
        queue.add(second);
        queue.add(third);
        TransferRequest actualFirst= queue.next();
        TransferRequest actualSecond= queue.next();
        TransferRequest actualThird= queue.next();
        assertEquals(first, actualFirst);
        assertEquals(second, actualSecond);
        assertEquals(third, actualThird);
        assertTrue(queue.isEmpty());
    }
    @Test
    void peekDontRemoveFirstRequest(){
        TransferQueue queue = new TransferQueue();
        TransferRequest request = new TransferRequest(
                new AccountNumber("0000000001"),
                new AccountNumber("0000000002"), 100
        );
        queue.add(request);
        TransferRequest result = queue.peek();
        assertEquals(request, result);
        assertEquals(1, queue.size());
        assertEquals(request, queue.next());
    }
    @Test
    void nextReturnsNullWhenQueueIsEmpty(){
        TransferQueue queue = new TransferQueue();
        TransferRequest result = queue.next();
        assertNull(result);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }
    @Test
    void sizeDecreasesAfterRetrievingRequest(){
        TransferQueue queue = new TransferQueue();
        AccountNumber from = new AccountNumber("0000000001");
        AccountNumber to = new AccountNumber("0000000002");
        queue.add(new TransferRequest(from,to,100));
        queue.add(new TransferRequest(from,to,200));
        assertEquals(2, queue.size());
        queue.next();
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());
    }
}
