package bank;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionFunctionsTest {
    @Test
    void filterKeepAccountsWithPOsitiveBalance() {
        BankAccount positive = new DebitAccount(
                new AccountNumber("1234567890"), "Sanya", 1000
        );
        BankAccount zero = new DebitAccount(
                new AccountNumber("1234567891"), "Саня", 0
        );
        BankAccount negative = new CreditAccount(
                new AccountNumber("1234567892"), "Саша", 0, 5000
        );
        negative.withdraw(1000);
        List<BankAccount> accounts = List.of(
                positive, zero, negative
        );
        Predicate<BankAccount> positiveBalance =
                account -> account.getBalance() > 0;
        List<BankAccount> result =
                CollectionFunctions.filter(accounts, positiveBalance);

        assertEquals(List.of(positive), result);
    }
    @Test
    void filterKeepsAccountsWithBalanceAtLeast100000() {
        BankAccount below = new DebitAccount(
                new AccountNumber("1234567890"), "S", 99_999
        );
        BankAccount boundary = new DebitAccount(
                new AccountNumber("1234567891"), "V", 100_000
        );
        BankAccount above = new DebitAccount(
                new AccountNumber("1234567892"), "W", 150_000
        );
        List<BankAccount> accounts = List.of(
                below, boundary, above
        );
        Predicate<BankAccount> rich =
                account -> account.getBalance() >= 100_000;
        List<BankAccount> result =
                CollectionFunctions.filter(accounts, rich);

        assertEquals(List.of(boundary, above), result);
    }

    @Test
    void filterKeepsOnlyDebitAccounts() {
        BankAccount debit = new DebitAccount(
                new AccountNumber("1234567890"), "S", 1000
        );
        BankAccount savings = new SavingsAccount(
                new AccountNumber("1234567891"), "A", 3000, 500
        );
        BankAccount credit = new CreditAccount(
                new AccountNumber("1234567892"), "D", 1000, 5000
        );
        List<BankAccount> accounts = List.of(
                debit, savings, credit
        );
        Predicate<BankAccount> debitOnly =
                DebitAccount.class::isInstance;
        List<BankAccount> result =
                CollectionFunctions.filter(accounts, debitOnly);

        assertEquals(List.of(debit), result);
    }
}