package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {
    @Test
    void registeredAccountCanBeFoundByNumber(){
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
    void registeringNewAccountsIncreasesAccountCount(){
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        BankService bank = new BankService(new InMemoryRepository<>(), transferService);
        BankAccount first = new DebitAccount(new AccountNumber("1234567890"), "sanya", 1000);
        BankAccount second = new SavingsAccount(new AccountNumber("1234567899"), "ALEX", 3000,500);
        assertEquals(0, bank.accountCount());
        bank.openAccount(first);
        assertEquals(1, bank.accountCount());
        bank.openAccount(second);
        assertEquals(2, bank.accountCount());
    }
    @Test
    void registeringSameIdReplaceceExistingAccount(){
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
    void closedAccountCanNoBeFound(){
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
    void closingMissingAccountDontChangeRegisteredACcount(){
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
    void openingNullAccountThrowsException(){
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
}
