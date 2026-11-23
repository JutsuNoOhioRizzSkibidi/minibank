package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransferApplicationServiceTest {
    @Test
    void moneyCanBeTransferred(){
        Repository<AccountNumber, BankAccount> accounts = new InMemoryRepository<>();
        BlockedAccountRegistry blockedAccounts = new BlockedAccountRegistry();
        TransactionJournal journal = new TransactionJournal();
        FakeNotification notification = new FakeNotification();

        TransferService transferService = new TransferService(
                new NoCommission(),
                notification,
                blockedAccounts,
                journal
        );
        TransferApplicationService service =
                new TransferApplicationService(accounts, transferService);

        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567891");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 10000);
        BankAccount to = new DebitAccount(toNumber, "sanya1", 2000);

        accounts.save(from);
        accounts.save(to);

        service.transfer(fromNumber, toNumber, 3000);

        assertEquals(7000.0, from.getBalance(), 0.01);
        assertEquals(5000.0, to.getBalance(), 0.01);
        assertEquals(1, journal.size());
        assertEquals(1, notification.getNotificationCount());
    }
    @Test
    void missingAccountCannotTransfer(){
        Repository<AccountNumber, BankAccount> accounts = new InMemoryRepository<>();
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        TransferApplicationService service =
                new TransferApplicationService(accounts, transferService);

        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567891");

        assertThrows(IllegalArgumentException.class,
                () -> service.transfer(fromNumber, toNumber, 1000));
    }
    @Test
    void transferLimitStillWorks(){
        Repository<AccountNumber, BankAccount> accounts = new InMemoryRepository<>();
        TransferService transferService = new TransferService(
                new NoCommission(),
                new FakeNotification(),
                new BlockedAccountRegistry(),
                new TransactionJournal()
        );
        TransferApplicationService service =
                new TransferApplicationService(accounts, transferService);

        AccountNumber fromNumber = new AccountNumber("1234567890");
        AccountNumber toNumber = new AccountNumber("1234567891");
        BankAccount from = new DebitAccount(fromNumber, "sanya", 100000);
        BankAccount to = new DebitAccount(toNumber, "sanya1", 2000);

        accounts.save(from);
        accounts.save(to);
        assertThrows(TransferLimitExceededException.class,
                () -> service.transfer(fromNumber, toNumber, 60000));

        assertEquals(100000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
}