package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransferServiceTest {
    @Test
    void successfulTransferUpdatesBothBalances() {
        BankAccount from =
                new DebitAccount("111", "john", 10000);
        BankAccount to =
                new DebitAccount("222", "pork", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 3000);
        assertTrue(result);
        assertEquals(7000.0, from.getBalance(), 0.01);
        assertEquals(5000.0, to.getBalance(), 0.01);
    }
    @Test
    void insufficientFundsLeavesBothBalancesUnchanged() {
        BankAccount from = new DebitAccount("333", "oleg", 1000);
        BankAccount to = new DebitAccount("444", "maria", 2000);

        TransferService service = new TransferService(
                new NoCommission(),
                new ConsoleNotificationService()
        );
        boolean result = service.transfer(from, to, 3000);
        assertFalse(result);
        assertEquals(1000.0, from.getBalance(), 0.01);
        assertEquals(2000.0, to.getBalance(), 0.01);
    }
}