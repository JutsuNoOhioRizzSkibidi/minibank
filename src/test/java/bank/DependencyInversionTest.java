package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DependencyInversionTest{
    @Test
    void repositoryCanBeReplaced(){
        Repository<AccountNumber, BankAccount> accounts = new InMemoryRepository<>();
        AccountService service = new AccountService(accounts);
        AccountNumber number = new AccountNumber("1234567890");
        BankAccount account = new DebitAccount(number, "sanya", 10000);

        service.open(account);
        assertTrue(service.find(number).isPresent());
        assertEquals(10000.0, service.find(number).get().getBalance(), 0.01);
    }
}