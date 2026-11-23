package bank;

import bank.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DefaultAccountFactoryTest{
    @Test
    void createDebitAccount(){
        AccountFactory factory = new DefaultAccountFactory();

        BankAccount account = factory.create(
                AccountType.DEBIT,
                new AccountNumber("1234567890"),
                "sanya",
                10000
        );
        assertTrue(account instanceof DebitAccount);
        assertEquals(10000, account.getBalance(), 0.01);
        assertEquals("sanya", account.getOwner());
    }

    @Test
    void createSavingsAccount(){
        AccountFactory factory = new DefaultAccountFactory();

        BankAccount account = factory.create(
                AccountType.SAVINGS,
                new AccountNumber("1234567891"),
                "sanya1",
                10000
        );
        assertTrue(account instanceof SavingsAccount);
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(9500));
        assertEquals(10000, account.getBalance(), 0.01);
    }

    @Test
    void createCreditAccount(){
        AccountFactory factory = new DefaultAccountFactory();

        BankAccount account = factory.create(
                AccountType.CREDIT,
                new AccountNumber("1234567892"),
                "sanya2",
                10000
        );
        assertTrue(account instanceof CreditAccount);
        account.withdraw(20000);
        assertEquals(-10000, account.getBalance(), 0.01);
    }
}