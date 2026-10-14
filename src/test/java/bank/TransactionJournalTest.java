package bank;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionJournalTest {
    @Test
    void transactionsAreStoredInInsertionOrder(){
        TransactionJournal journal = new TransactionJournal();
        Transaction first = new Transaction(
                1L,
                TransactionType.DEPOSIT,
                new AccountNumber("0000000001"),
                1000,
                TransactionStatus.SUCCESS
        );
        Transaction second = new Transaction(
                2L,
                TransactionType.WITHDRAWAL,
                new AccountNumber("0000000001"),
                500,
                TransactionStatus.SUCCESS
        );
        journal.add(first);
        journal.add(second);
        assertEquals(2, journal.size());
        assertEquals(List.of(first, second), journal.findAll());
    }
    @Test
    void identicalTransactionsCanBeStoredTwice(){
        TransactionJournal journal = new TransactionJournal();
        Transaction transaction = new Transaction(
                1L,
                TransactionType.TRANSFER,
                new AccountNumber("0000000001"),
                300,
                TransactionStatus.SUCCESS
        );
        journal.add(transaction);
        journal.add(transaction);
        assertEquals(2, journal.size());
        assertEquals(List.of(transaction, transaction), journal.findAll());
    }
    @Test
    void returnedListDontExposeInternalState(){
        TransactionJournal journal = new TransactionJournal();
        Transaction transaction = new Transaction(
                1L,
                TransactionType.DEPOSIT,
                new AccountNumber("0000000001"),
                1000,
                TransactionStatus.SUCCESS
        );
        journal.add(transaction);
        List<Transaction> copy = journal.findAll();
        copy.clear();
        assertTrue(copy.isEmpty());
        assertEquals(1, journal.size());
        assertEquals(List.of(transaction), journal.findAll());
    }

}
