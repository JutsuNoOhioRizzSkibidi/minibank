package bank;

import bank.adapters.InMemoryRepository;
import bank.application.AccountService;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.domain.DebitAccount;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {
    @Test
    void accountCanBeOpenedAndFound(){
        AccountService service = new AccountService(new InMemoryRepository<>());
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(number, "sanya", 1000);

        service.open(account);

        assertSame(account, service.find(number).orElseThrow());
    }
    @Test
    void accountCanBeClosed(){
        AccountService service = new AccountService(new InMemoryRepository<>());
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(number, "sanya", 1000);

        service.open(account);

        assertTrue(service.close(number));
        assertTrue(service.find(number).isEmpty());
    }
    @Test
    void missingAccountReturnsEmptyOptional(){
        AccountService service = new AccountService(new InMemoryRepository<>());

        assertTrue(service.find(new AccountNumber("1234567890")).isEmpty());
    }
}