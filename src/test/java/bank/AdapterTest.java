package bank;

import bank.adapters.ConsoleNotificationService;
import bank.adapters.InMemoryRepository;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.DebitAccount;
import bank.ports.NotificationService;
import bank.ports.Repository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdapterTest{
    @Test
    void repositoryAdapterTest(){
        Repository<AccountNumber, BankAccount> accounts = new InMemoryRepository<>();
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(number, "sanya", 10000);

        accounts.save(account);
        assertEquals(account, accounts.findById(number));
        assertEquals(1, accounts.size());
    }
    @Test
    void notificationAdapterTest(){
        NotificationService notification = new ConsoleNotificationService();
        assertDoesNotThrow(() -> notification.notify("перевод выполнен"));
    }
}