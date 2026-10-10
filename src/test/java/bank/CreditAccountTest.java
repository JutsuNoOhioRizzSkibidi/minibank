package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class CreditAccountTest {
    @Test
    void canWithdrawToCreditLimit() {
        CreditAccount account =
                new CreditAccount(new AccountNumber("0000000001"), "Иван", 1000, 5000);
        boolean result = account.withdraw(6000);
        assertTrue(result);
        assertEquals(-5000.0, account.getBalance(), 0.01);
    }
    @Test
    void cantWithdrawBeyondCreditLimit() {
        CreditAccount account =
                new CreditAccount(new AccountNumber("0000000002"), "Ольга", 1000, 5000);
        boolean result = account.withdraw(6001);
        assertFalse(result);
        assertEquals(1000.0, account.getBalance(), 0.001);
    }

}