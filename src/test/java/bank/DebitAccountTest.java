package bank;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitAccountTest {
    @Test
    void cantWithdrawMoreThanBalance(){
        DebitAccount account =
                new DebitAccount("111", "Алексей", 1000);
        boolean result = account.withdraw(1500);
        assertFalse(result);
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void withdrawWithinBalanceReducesBalance(){
        DebitAccount account =
                new DebitAccount("222", "Олег", 1000);
        boolean result = account.withdraw(500);
        assertTrue(result);
        assertEquals(500, account.getBalance(), 0.01);
    }
    @Test
    void initialBalanceIsSaved() {
        double initialBalance = 1000;
        DebitAccount account =
                new DebitAccount("333", "Вася", initialBalance);
        assertEquals(initialBalance, account.getBalance(), 0.01);
    }
    @Test
    void depositIncreasesBalance(){
        DebitAccount account =
                new DebitAccount("444", "Гоша", 1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance(), 0.01);
    }
    @Test
    void noDepositNoIncreaseBalance(){
        DebitAccount account =
                new DebitAccount("555", "Гена", 1000);
        account.deposit(0);
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void negativeDepositNoIncreaseBalance(){
        DebitAccount account =
                new DebitAccount("666", "Ваня", 1000);
        account.deposit(-100);
        assertEquals(1000, account.getBalance(), 0.01);
    }
    @Test
    void cantWithdrawNoAmount(){
        DebitAccount account =
                new DebitAccount("777", "john", 1000);
        boolean result = account.withdraw(0);
        assertFalse(result);
        assertEquals(1000, account.getBalance(),0.01);
    }
    @Test
    void cantWithdrawNegativeAmount(){
        DebitAccount account =
                new DebitAccount("888", "Lisa", 1000);
        boolean result = account.withdraw(-1);
        assertFalse(result);
        assertEquals(1000, account.getBalance(), 0.01);
    }
}
