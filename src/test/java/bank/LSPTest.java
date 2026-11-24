package bank;

import bank.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LSPTest{
    @Test
    void allAccountsCanWithdraw(){
        BankAccount[] accounts = {
                new DebitAccount(new AccountNumber("1234567890"), "sanya", 10000),
                new SavingsAccount(new AccountNumber("1234567891"), "sanya1", 10000, 1000),
                new CreditAccount(new AccountNumber("1234567892"), "sanya2", 10000, 50000)
        };

        for (BankAccount account : accounts){
            account.withdraw(1000);
            assertEquals(9000, account.getBalance(), 0.01);
        }
    }
    @Test
    void allAccountsRejectNegativeAmount(){
        BankAccount[] accounts = {
                new DebitAccount(new AccountNumber("1234567890"), "sanya", 10000),
                new SavingsAccount(new AccountNumber("1234567891"), "sanya1", 10000, 1000),
                new CreditAccount(new AccountNumber("1234567892"), "sanya2", 10000, 50000)
        };

        for (BankAccount account : accounts){
            assertThrows(IllegalArgumentException.class,
                    () -> account.withdraw(-1000));
            assertEquals(10000, account.getBalance(), 0.01);
        }
    }
    @Test
    void allAccountsRejectTooLargeAmount(){
        BankAccount[] accounts = {
                new DebitAccount(new AccountNumber("1234567890"), "sanya", 10000),
                new SavingsAccount(new AccountNumber("1234567891"), "sanya1", 10000, 1000),
                new CreditAccount(new AccountNumber("1234567892"), "sanya2", 10000, 50000)
        };

        for (BankAccount account : accounts){
            assertThrows(InsufficientFundsException.class,
                    () -> account.withdraw(100000));
            assertEquals(10000, account.getBalance(), 0.01);
        }
    }
}