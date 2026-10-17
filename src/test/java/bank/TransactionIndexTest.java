package bank;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionIndexTest {
    @Test
    void transactionCanBeFoundByEqualAccountNum(){
        TransactionIndex index = new TransactionIndex();
        AccountNumber stored = new AccountNumber("1234567890");
        AccountNumber lookup = new AccountNumber("1234567890");
        Transaction tx = new Transaction(
                1L,
                TransactionType.TRANSFER,
                stored,
                1000,
                TransactionStatus.SUCCESS
        );
        index.add(tx);
        List<Transaction> found = index.findByAccount(lookup);
        assertEquals(1, found.size());
        assertSame(tx, found.get(0));
    }
    @Test
    void changingRetunedListDontChangeIndex(){
        TransactionIndex index = new TransactionIndex();
        AccountNumber number = new AccountNumber("1234567890");
        Transaction tx = new Transaction(
                1L,
                TransactionType.TRANSFER,
                number,
                1000,
                TransactionStatus.SUCCESS
        );
        index.add(tx);
        List<Transaction> copy = index.findByAccount(number);
        copy.clear();
        assertEquals(List.of(tx), index.findByAccount(number));
    }
}
