package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SavingsAccountTest {
    @Test
    void cannotWithdrawBelowMinimumBalance() {
        SavingsAccount account =
                new SavingsAccount("111", "Андрей", 2000, 1000);
        boolean result = account.withdraw(1500);
        assertFalse(result);
        assertEquals(2000.0, account.getBalance(), 0.01);
    }
    @Test
    void canWithdrawToMinimumBalance() {
        SavingsAccount account =
                new SavingsAccount("222", "Аюша", 2000, 1000);
        boolean result = account.withdraw(1000);
        assertTrue(result);
        assertEquals(1000.0, account.getBalance(), 0.01);
    }
}