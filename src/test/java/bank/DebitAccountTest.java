package bank;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DebitAccountTest {
    @Test
    void cantWithdrawMoreThanBalance(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000001"), "Алексей", 1000);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(1500));
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void withdrawWithinBalanceReducesBalance(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000002"), "Олег", 1000);
        account.withdraw(500);
        assertEquals(500, account.getBalance(), 0.01);
    }
    @Test
    void initialBalanceIsSaved() {
        double initialBalance = 1000;
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000003"), "Вася", initialBalance);
        assertEquals(initialBalance, account.getBalance(), 0.01);
    }
    @Test
    void depositIncreasesBalance(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000004"), "Гоша", 1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance(), 0.01);
    }
    @Test
    void noDepositNoIncreaseBalance(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000005"), "Гена", 1000);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void negativeDepositThrowsException() {
        BankAccount account = new DebitAccount(
                new AccountNumber("0000000010"), "Анна", 1000
        );
        assertThrows(
                IllegalArgumentException.class, () -> account.deposit(-100)
        );
        assertEquals(1000.0, account.getBalance(), 0.001);
    }
    @Test
    void cantWithdrawNoAmount(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000007"), "john", 1000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertEquals(1000, account.getBalance(),0.01);
    }
    @Test
    void cantWithdrawNegativeAmount(){
        DebitAccount account =
                new DebitAccount(new AccountNumber("0000000009"), "Lisa", 1000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-500));
        assertEquals(1000, account.getBalance(), 0.01);
    }
}
