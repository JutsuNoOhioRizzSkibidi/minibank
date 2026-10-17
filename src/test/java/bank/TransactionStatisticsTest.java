package bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionStatisticsTest {
    @Test
    void emptyJournalProducesEmptyReports(){
        TransactionStatistics statistics = new TransactionStatistics();
        TransactionJournal journal = new TransactionJournal();
        Map<AccountNumber, Integer> counts =
                statistics.countByAccount(journal.findAll());
        Map<AccountNumber, Double> totals =
                statistics.totalAmountByAccount(journal.findAll());
        assertTrue(counts.isEmpty());
        assertTrue(totals.isEmpty());
    }
    @Test
    void singleAccountWithOneTransactionIsreported(){
        TransactionStatistics statistics = new TransactionStatistics();
        AccountNumber number = new AccountNumber("1234567890");
        List<Transaction> transactions  = List.of(new Transaction(
                1L,
                TransactionType.TRANSFER,
                number,
                1000,
                TransactionStatus.SUCCESS
        ));
        Map<AccountNumber, Integer> counts =
                statistics.countByAccount(transactions);
        Map<AccountNumber, Double> totals =
                statistics.totalAmountByAccount(transactions);
        assertEquals(Map.of(number, 1), counts);
        assertEquals(1, totals.size());
        assertEquals(1000.0, totals.get(number), 0.01);
    }
    @Test
    void multipleTransactionsOfOneAccountAreAggreg(){
        TransactionStatistics statistics = new TransactionStatistics();
        AccountNumber number = new AccountNumber("1234567890");
        List<Transaction> transactions = List.of(new Transaction(
                1L,
                TransactionType.TRANSFER,
                number,
                1000,
                TransactionStatus.SUCCESS
                ),
                new Transaction(
                        2L,
                        TransactionType.TRANSFER,
                        number,
                        2500,
                        TransactionStatus.SUCCESS
                ),
                new Transaction(
                        3L,
                        TransactionType.TRANSFER,
                        number,
                        500,
                        TransactionStatus.SUCCESS
                )

        );
        Map<AccountNumber, Integer> counts =
                statistics.countByAccount(transactions);
        Map<AccountNumber, Double> totals =
                statistics.totalAmountByAccount(transactions);
        assertEquals(Map.of(number, 3), counts);
        assertEquals(1, totals.size());
        assertEquals(4000.0,totals.get(number), 0.01);
    }
    @Test
    void transactionsOfDifferentAccountsAreAggregatedSeparetely(){
        TransactionStatistics statistics = new TransactionStatistics();
        AccountNumber first = new AccountNumber("1234567890");
        AccountNumber second = new AccountNumber("1234567899");
        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        TransactionType.TRANSFER,
                        first,
                        1000,
                        TransactionStatus.SUCCESS
                ),
                new Transaction(
                        2L,
                        TransactionType.TRANSFER,
                        second,
                        500,
                        TransactionStatus.SUCCESS
                ),
                new Transaction(
                        3L,
                        TransactionType.TRANSFER,
                        first,
                        2000,
                        TransactionStatus.SUCCESS
                )

        );
        Map<AccountNumber, Integer> counts =
                statistics.countByAccount(transactions);
        Map<AccountNumber, Double> totals =
                statistics.totalAmountByAccount(transactions);
        assertEquals(Map.of(first, 2, second, 1), counts);
        assertEquals(2, totals.size());
        assertEquals(3000.0, totals.get(first), 0.01);
        assertEquals(500.0, totals.get(second), 0.01);
    }
    @Test
    void equalAccountNumberAreAggregatedIntoONeEntry(){
        TransactionStatistics statistics = new TransactionStatistics();
        AccountNumber first = new AccountNumber("1234567890");
        AccountNumber second = new AccountNumber("1234567890");
        List<Transaction> transactions = List.of(
                new Transaction(
                        1L,
                        TransactionType.TRANSFER,
                        first,
                        1000,
                        TransactionStatus.SUCCESS
                ),
                new Transaction(
                        2L,
                        TransactionType.TRANSFER,
                        second,
                        2500,
                        TransactionStatus.SUCCESS
                )
        );
        Map<AccountNumber, Integer> counts =
                statistics.countByAccount(transactions);
        Map<AccountNumber, Double> totals =
                statistics.totalAmountByAccount(transactions);
        assertEquals(Map.of(first, 2), counts);
        assertEquals(1, totals.size());
        assertEquals(3500.0, totals.get(first), 0.01);
    }

}
