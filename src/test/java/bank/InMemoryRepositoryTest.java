package bank;

import org.junit.jupiter.api.Test;

import javax.management.ImmutableDescriptor;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class InMemoryRepositoryTest {
    @Test
    void newRepositoryIsEmpty(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        int size = repository.size();
        assertEquals(0, size);
    }
    @Test
    void savedAccountCanBeFindById(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        AccountNumber number = new AccountNumber("0000000001");
        BankAccount account = new DebitAccount(number, "Stas", 1000);
        repository.save(account);
        assertEquals(1, repository.size());
        assertTrue(repository.existsById(number));
        assertSame(account, repository.findById(
                new AccountNumber("0000000001")
        ));
    }
    @Test
    void savingSameIdReplacesExistingAccount(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        BankAccount original = new DebitAccount(
                new AccountNumber("0000000002"), "Valya", 1000);
        BankAccount replacment = new DebitAccount(
                new AccountNumber("0000000002"), "Valya", 2000);
        repository.save(original);
        repository.save(replacment);
        assertEquals(1, repository.size());
        assertSame(
                replacment, repository.findById(new AccountNumber("0000000002"))
        );
    }
    @Test
    void savingNullThrowsException() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
        assertEquals(0, repository.size());
    }
    @Test
    void findByIdReturnsNullWhenAccountIsMissing() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        repository.save(new DebitAccount(
                new AccountNumber("0000000003"), "Jeka", 1000));
        BankAccount result =
                repository.findById(new AccountNumber("0000000001"));
        assertNull(result);
    }
    @Test
    void existsByIdReturnsFalseWhenAccountIsMissing() {
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        repository.save(new DebitAccount(
                new AccountNumber("0000000001"), "Sanya", 1000));
        boolean exists =
                repository.existsById(new AccountNumber("0000000002"));
        assertFalse(exists);
    }
    @Test
    void deleteByIdRemovesExistAccount(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        AccountNumber id = new AccountNumber("0000000001");
        repository.save(new DebitAccount(id, "sanya", 1000));
        boolean deleted = repository.deleteById(id);
        assertTrue(deleted);
        assertFalse(repository.existsById(id));
        assertNull(repository.findById(id));
        assertEquals(0, repository.size());
    }
    @Test
    void deleteByIdReturnsFalseWhenAccountIsMiss(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        AccountNumber existingId = new AccountNumber("0000000001");
        BankAccount account = new DebitAccount(existingId, "ALEX", 2000);
        repository.save(account);
        boolean deleted = repository.deleteById(new AccountNumber("0000000002"));
        assertFalse(deleted);
        assertEquals(1, repository.size());
        assertSame(account, repository.findById(existingId));

    }
    @Test
    void findAllReturnsAllSavedAccounts(){
        Repository<AccountNumber, BankAccount> repository =
                new InMemoryRepository<>();
        BankAccount first = new DebitAccount(
                new AccountNumber("0000000001"), "David", 1000
        );
        BankAccount second = new SavingsAccount(new AccountNumber("0000000002"), "Ulu", 3000,500);
        repository.save(first);
        repository.save(second);
        List<BankAccount> accounts = repository.findAll();
        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(first));
        assertTrue(accounts.contains(second));
    }
}
