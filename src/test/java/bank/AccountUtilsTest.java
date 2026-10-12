package bank;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Test
    void demoAccountsCanBeAddedToDebitAccountList() {
        List<DebitAccount> accounts = new ArrayList<>();
        AccountUtils.addDemoDebitAccounts(accounts);
        assertEquals(2, accounts.size());
        assertEquals(new AccountNumber("0000000001"), accounts.get(0).getId());
        assertEquals(new AccountNumber("0000000002"), accounts.get(1).getId());
    }
    @Test
    void demoAccountsCanBeAddedToBankAccountList() {
        List<BankAccount> accounts = new ArrayList<>();
        BankAccount existing = new SavingsAccount(new AccountNumber("0000000011"), "Аяна", 5000, 1000
        );
        accounts.add(existing);
        AccountUtils.addDemoDebitAccounts(accounts);
        assertEquals(3, accounts.size());
        assertEquals(existing, accounts.get(0));
        assertInstanceOf(DebitAccount.class, accounts.get(1));
        assertInstanceOf(DebitAccount.class, accounts.get(2));
    }
    @Test
    void demoAccountsCanBeAddedToObjectList() {
        List<Object> values = new ArrayList<>();
        values.add("привет");
        AccountUtils.addDemoDebitAccounts(values);
        assertEquals(3, values.size());
        assertEquals("привет", values.get(0));
        assertInstanceOf(DebitAccount.class, values.get(1));
        assertInstanceOf(DebitAccount.class, values.get(2));
    }
    @Test
    void copyDebitAccountsToBankAccountList() {
        DebitAccount account = new DebitAccount(new AccountNumber("0000000012"), "fgfg", 1000);
        List<DebitAccount> source = List.of(account);
        List<BankAccount> target = new ArrayList<>();
        AccountUtils.copy(source, target);
        assertEquals(1, target.size());
        assertSame(account, target.get(0));
        assertEquals(1, source.size());
    }
    @Test
    void copyDebitAccountsToObjectList() {
        DebitAccount account = new DebitAccount(new AccountNumber("0000000013"), "sadsadsa", 2000);
        List<DebitAccount> source = List.of(account);
        List<Object> target = new ArrayList<>();
        target.add("begin");
        AccountUtils.copy(source, target);
        assertEquals(2, target.size());
        assertEquals("begin", target.get(0));
        assertSame(account, target.get(1));
    }
    @Test
    void copyBankAccountsPreservesElementOrder() {
        BankAccount first = new DebitAccount(new AccountNumber("0000000014"), "dfgfdgdf", 1000);
        BankAccount second = new SavingsAccount(new AccountNumber("0000000015"), "dfgdfgfdg", 3000, 500);
        BankAccount third = new CreditAccount(new AccountNumber("0000000016"), "dfgfdgfd", 2000, 5000);
        List<BankAccount> source = List.of(first, second, third);
        List<BankAccount> target = new ArrayList<>();
        AccountUtils.copy(source, target);
        assertEquals(3, target.size());
        assertSame(first, target.get(0));
        assertSame(second, target.get(1));
        assertSame(third, target.get(2));
    }
    @Test
    void richestReturnsAccountWithMaximumBalance() {
        DebitAccount first = new DebitAccount(new AccountNumber("0000000017"), "dfd", 1000);
        DebitAccount second = new DebitAccount(new AccountNumber("0000000018"), "dfdd", 5000);
        DebitAccount third = new DebitAccount(new AccountNumber("0000000019"), "asdas", 2000);
        List<DebitAccount> accounts = List.of(first, second, third);
        DebitAccount result = AccountUtils.richest(accounts);
        assertSame(second, result);
    }
    @Test
    void richestRejectsEmptyList() {
        List<DebitAccount> accounts = List.of();
        assertThrows(IllegalArgumentException.class,
                () -> AccountUtils.richest(accounts));
    }
    @Test
    void richestReturnsOnlyAccount() {
        SavingsAccount account = new SavingsAccount(new AccountNumber("0000000020"), "dsfdsf", 3000, 1000);
        List<SavingsAccount> accounts = List.of(account);
        SavingsAccount result = AccountUtils.richest(accounts);
        assertSame(account, result);
    }
    @Test
    void richestReturnsFirstAccountWhenMaximumBalancesAreEqual() {
        DebitAccount smaller = new DebitAccount(new AccountNumber("0000000021"), "ddddd", 1000);
        DebitAccount firstMaximum = new DebitAccount(new AccountNumber("0000000022"), "ssss", 5000);
        DebitAccount secondMaximum = new DebitAccount(new AccountNumber("0000000023"), "qqqq", 5000);
        List<DebitAccount> accounts = List.of(smaller, firstMaximum, secondMaximum);
        DebitAccount result = AccountUtils.richest(accounts);
        assertSame(firstMaximum, result);
    }

}
