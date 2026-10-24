package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Comparator;

public class BankServiceTest {
    @Test
    void registeredAccountCanBeFoundByNumber() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(number, "sanya", 1000);
        bank.openAccount(account);
        BankAccount found = bank.findAccount(new AccountNumber("1234567890"));
        assertSame(account, found);
    }

    @Test
    void registeringNewAccountsIncreasesAccountCount() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        BankAccount first = new DebitAccount(new AccountNumber("1234567890"), "sanya", 1000);
        BankAccount second = new SavingsAccount(new AccountNumber("1234567899"), "ALEX", 3000, 500);
        assertEquals(0, bank.accountCount());
        bank.openAccount(first);
        assertEquals(1, bank.accountCount());
        bank.openAccount(second);
        assertEquals(2, bank.accountCount());
    }

    @Test
    void registeringSameIdReplaceceExistingAccount() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount original = new DebitAccount(number, "sanya", 1000);
        BankAccount replacement = new DebitAccount(new AccountNumber("1234567890"), "jojo", 2000);
        bank.openAccount(original);
        bank.openAccount(replacement);
        assertEquals(1, bank.accountCount());
        assertSame(replacement, bank.findAccount(number));
    }

    @Test
    void closedAccountCanNoBeFound() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        AccountNumber number = new AccountNumber("1234567890");
        bank.openAccount(new DebitAccount(number, "sanya", 1000));
        boolean closed = bank.closeAccount(number);
        assertTrue(closed);
        assertNull(bank.findAccount(number));
        assertEquals(0, bank.accountCount());
    }

    @Test
    void closingMissingAccountDontChangeRegisteredACcount() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        AccountNumber existingNumber = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(existingNumber, "sanya", 2000);
        bank.openAccount(account);
        boolean closed = bank.closeAccount(new AccountNumber("1234567899"));
        assertFalse(closed);
        assertEquals(1, bank.accountCount());
        assertSame(account, bank.findAccount(existingNumber));
    }

    @Test
    void openingNullAccountThrowsException() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        assertThrows(IllegalArgumentException.class, () -> bank.openAccount(null));
        assertEquals(0, bank.accountCount());
    }

    @Test
    void transferByAccountNumberUpdatesBothBalance() {
        FakeNotification notification = new FakeNotification();
        TransactionJournal journal = new TransactionJournal();
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                journal
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567899");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 10000);
        BankAccount to = new DebitAccount(toNumber, "Aalex", 2000);
        bank.openAccount(from);
        bank.openAccount(to);
        bank.transfer(fromNumber, toNumber, 3000);
        assertEquals(7000.0, from.getBalance(), 0.01);
        assertEquals(5000.0, to.getBalance(), 0.01);
        assertEquals(1, notification.getNotificationCount());
        assertEquals(1, journal.size());
    }

    @Test
    void transferWithMissingSenderDoesNotChangeState() {
        FakeNotification notification = new FakeNotification();
        TransactionJournal journal = new TransactionJournal();
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                journal
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        AccountNumber missingNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567899");
        BankAccount to = new DebitAccount(toNumber, "sanya", 2000);
        bank.openAccount(to);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(missingNumber, toNumber, 1000));
        assertEquals("счет отправителя не найден", exception.getMessage());
        assertEquals(2000.0, to.getBalance(), 0.01);
        assertEquals(1, bank.accountCount());
        assertEquals(0, notification.getNotificationCount());
        assertEquals(0, journal.size());
    }

    @Test
    void transferWithMissingRecipientDOntChangeState() {
        FakeNotification notification = new FakeNotification();
        TransactionJournal journal = new TransactionJournal();
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                journal
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber missingNumber = new AccountNumber("1234567899");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 5000);
        bank.openAccount(from);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bank.transfer(fromNumber, missingNumber, 1000));
        assertEquals("счет получателя не найден", exception.getMessage());
        assertEquals(5000.0, from.getBalance(), 0.01);
        assertEquals(1, bank.accountCount());
        assertEquals(0, notification.getNotificationCount());
        assertEquals(0, journal.size());
    }

    @Test
    void transferFromBlockedAccountDontChangeState() {
        FakeNotification notification = new FakeNotification();
        BlockedAccountRegistry blockedAccount = new BlockedAccountRegistry();
        TransactionJournal journal = new TransactionJournal();
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                blockedAccount,
                journal
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567899");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 5000);
        BankAccount to = new DebitAccount(toNumber, "Alex", 2000);
        bank.openAccount(from);
        bank.openAccount(to);
        blockedAccount.block(fromNumber);
        assertThrows(IllegalStateException.class,
                () -> bank.transfer(fromNumber, toNumber, 1000));
        assertEquals(5000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
        assertEquals(0, notification.getNotificationCount());
        assertEquals(0, journal.size());
    }

    @Test
    void transferWithoutMoneyDontChangeState() {
        FakeNotification notification = new FakeNotification();
        TransactionJournal journal = new TransactionJournal();
        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                new BlockedAccountRegistry(),
                journal
        );
        BankService bank = new BankService(new InMemoryRepository<>(),
                transferService);
        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567899");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 1000);
        BankAccount to = new DebitAccount(toNumber, "alex", 2000);
        bank.openAccount(from);
        bank.openAccount(to);
        assertThrows(InsufficientFundsException.class,
                () -> bank.transfer(fromNumber, toNumber, 3000));
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
        assertEquals(0, notification.getNotificationCount());
        assertEquals(0, journal.size());
    }

    @Test
    void findAccountReturnesFilteredListWithoutExpStorage() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(), transferService
        );
        BankAccount positive = new DebitAccount(
                new AccountNumber("1234567890"), "S", 1000
        );
        BankAccount zero = new DebitAccount(
                new AccountNumber("1234567899"), "K", 0
        );
        bank.openAccount(positive);
        bank.openAccount(zero);
        List<BankAccount> result = bank.findAccounts(
                account -> account.getBalance() > 0
        );

        assertEquals(List.of(positive), result);
        result.clear();
        assertEquals(2, bank.accountCount());
        assertSame(positive, bank.findAccount(positive.getId()));
        assertSame(zero, bank.findAccount(zero.getId()));
    }

    @Test
    void aaccountsAreSortedByBalanceAscending() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(), transferService
        );
        BankAccount first = new DebitAccount(
                new AccountNumber("1234567890"), "Sanya", 5000
        );
        BankAccount second = new DebitAccount(
                new AccountNumber("1234567891"), "Sanya1", 1000
        );
        BankAccount third = new DebitAccount(
                new AccountNumber("1234567892"), "Sanya2", 3000
        );
        bank.openAccount(first);
        bank.openAccount(second);
        bank.openAccount(third);

        Comparator<BankAccount> byBalance =
                Comparator.comparingDouble(BankAccount::getBalance);
        List<BankAccount> result = bank.sortedAccounts(byBalance);
        assertEquals(List.of(second, third, first), result);

    }

    @Test
    void accountsAreSortedByBalanceDescending() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(), transferService
        );
        BankAccount first = new DebitAccount(
                new AccountNumber("1234567890"), "sanya1", 5000
        );
        BankAccount second = new DebitAccount(
                new AccountNumber("1234567891"), "sanya2", 1000
        );
        BankAccount third = new DebitAccount(
                new AccountNumber("1234567892"), "sanya3", 2000
        );
        bank.openAccount(first);
        bank.openAccount(second);
        bank.openAccount(third);
        Comparator<BankAccount> byBalance =
                Comparator.comparingDouble(BankAccount::getBalance);
        Comparator<BankAccount> byBalanceDesc =
                byBalance.reversed();
        List<BankAccount> result = bank.sortedAccounts(byBalanceDesc);
        assertEquals(List.of(first, third, second), result);
        result.clear();
        assertEquals(3, bank.accountCount());
        assertSame(first, bank.findAccount(first.getId()));
        assertSame(second, bank.findAccount(second.getId()));
        assertSame(third, bank.findAccount(third.getId()));
    }

    @Test
    void accountsAreSortedByNumber() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(), transferService
        );
        BankAccount first = new DebitAccount(
                new AccountNumber("1234567890"), "sanya1", 1000
        );
        BankAccount second = new DebitAccount(
                new AccountNumber("1234567899"), "sanya2", 5000
        );
        BankAccount third = new DebitAccount(
                new AccountNumber("1234567898"), "sanya3", 3000
        );
        bank.openAccount(first);
        bank.openAccount(second);
        bank.openAccount(third);
        Comparator<BankAccount> byNumber =
                Comparator.comparing(a -> a.getId().value());
        List<BankAccount> result = bank.sortedAccounts(byNumber);
        assertEquals(List.of(first, third, second), result);
    }

    @Test
    void accountsAreSortedByTypeThenBalance() {
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(), transferService
        );
        BankAccount debitHigh = new DebitAccount(
                new AccountNumber("1234567890"), "Sanya", 5000
        );
        BankAccount savings = new SavingsAccount(
                new AccountNumber("1234567899"), "Alexandr", 1000, 500
        );
        BankAccount credit = new CreditAccount(
                new AccountNumber("1234567898"), "Jojo", 9000, 5000
        );
        BankAccount debitLow = new DebitAccount(
                new AccountNumber("1234567897"), "Zedan", 2000
        );
        bank.openAccount(debitHigh);
        bank.openAccount(savings);
        bank.openAccount(credit);
        bank.openAccount(debitLow);
        Comparator<BankAccount> byTypeThenBalance =
                Comparator.comparing((BankAccount a) -> a.getClass().getSimpleName()
                ).thenComparingDouble(BankAccount::getBalance);
        List<BankAccount> result = bank.sortedAccounts(byTypeThenBalance);
        assertEquals(List.of(credit, debitLow, debitHigh, savings), result);
    }
}