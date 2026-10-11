package bank;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountUtilsTest {
    @Test
    void totalBalanceAcceptDebitAccounts(){
        List<DebitAccount> accounts = List.of(
                new DebitAccount(new AccountNumber("0000000001"), "Olya", 1000),
                new DebitAccount(new AccountNumber("0000000002"), "Kirill", 2000));
        double total = AccountUtils.totalBalance(accounts);
        assertEquals(3000.0, total, 0.01);
    }
    @Test
    void totalBalanceAcceptsSavingsAccounts(){
        List<SavingsAccount> accounts = List.of(
                new SavingsAccount(new AccountNumber("0000000003"), "Jeka", 5000, 1000),
                new SavingsAccount(new AccountNumber("0000000004"), "HollidaY", 3000,500)
        );
        double total = AccountUtils.totalBalance(accounts);
        assertEquals(8000.0, total, 0.01);
    }
    @Test
    void totalBalanceAcceptsCreditAccounts() {
        CreditAccount account = new CreditAccount(
                new AccountNumber("0000000005"), "Petr", 1000, 5000
        );
        account.withdraw(3000);
        List<CreditAccount> accounts = List.of(account);
        double total = AccountUtils.totalBalance(accounts);
        assertEquals(-2000.0, total, 0.01);
    }
    @Test
    void totalBalanceAcceptsDifferentAccountTypes() {
        List<BankAccount> accounts = List.of(
                new DebitAccount(new AccountNumber("0000000006"), "Иван", 1000),
                new SavingsAccount(new AccountNumber("0000000007"), "Анна", 3000, 1000),
                new CreditAccount(new AccountNumber("0000000008"), "Олег", 2000, 5000)
        );
        double total = AccountUtils.totalBalance(accounts);
        assertEquals(6000.0, total, 0.01);
    }
    @Test
    void totalBalanceOfEmptyListIsZero() {
        List<BankAccount> accounts = List.of();
        double total = AccountUtils.totalBalance(accounts);
        assertEquals(0.0, total, 0.01);
    }
}
