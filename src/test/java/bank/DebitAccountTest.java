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
}
