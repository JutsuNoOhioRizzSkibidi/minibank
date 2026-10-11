package bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankAccountTest {
    @Test
    void sameNumberAccountsAreEqualByDifType() {
        BankAccount debit =
                new DebitAccount(new AccountNumber("0000000001"), "Иван", 1000);
        BankAccount savings =
                new SavingsAccount(new AccountNumber("0000000001"), "Анна", 2000, 500);
        BankAccount credit =
                new CreditAccount(new AccountNumber("0000000001"), "Олег", 3000, 5000);
        boolean debitEqualsSavings = debit.equals(savings);
        boolean savingsEqualsDebit = savings.equals(debit);
        boolean debitEqualsCredit = debit.equals(credit);
        boolean creditEqualsDebit = credit.equals(debit);
        boolean savingsEqualsCredit = savings.equals(credit);
        boolean creditEqualsSavings = credit.equals(savings);
        assertEquals(true, debitEqualsSavings);
        assertEquals(true, savingsEqualsDebit);
        assertEquals(true, debitEqualsCredit);
        assertEquals(true, creditEqualsDebit);
        assertEquals(true, savingsEqualsCredit);
        assertEquals(true, creditEqualsSavings);
        assertEquals(debit.hashCode(), savings.hashCode());
        assertEquals(debit.hashCode(), credit.hashCode());
    }
    @Test
    void accountsWithDifferentNumbersAreNotEqual() {
        BankAccount first =
                new DebitAccount(new AccountNumber("0000000001"), "Иван", 1000);
        BankAccount second =
                new DebitAccount(new AccountNumber("0000000002"), "Иван", 1000);
        boolean result = first.equals(second);
        assertFalse(result);
    }
    @Test
    void balanceChangeDontChangeHashCode(){
        BankAccount first =
                new DebitAccount(new AccountNumber("0000000003"), "Aleksey", 1000);
        BankAccount second =
                new DebitAccount(new AccountNumber("0000000003"), "Aleksey", 1000);
        int originalHashCode = first.hashCode();
        first.deposit(500);
        assertEquals(1500, first.getBalance(), 0.01);
        assertEquals(first, second);
        assertEquals(originalHashCode, first.hashCode());
        assertEquals(first.hashCode(), second.hashCode());
    }
    @Test
    void accountEqualsItself(){
        BankAccount account =
                new DebitAccount(new AccountNumber("0000000004"), "Henry", 1000);
        boolean result = account.equals(account);
        assertTrue(result);
    }
    @Test
    void accountDoesNotEqualNull(){
        BankAccount account =
                new DebitAccount(new AccountNumber("0000000005"), "Vitaliy", 1000);
        boolean result = account.equals(null);
        assertFalse(result);
    }
    @Test
    void equalAccountsHaveSameHashCode(){
        BankAccount first =
                new DebitAccount(new AccountNumber("0000000006"), "Pavel", 1000);
        BankAccount second =
                new CreditAccount(new AccountNumber("0000000006"), "Pavel", 2000, 5000);
        int firstHashCode = first.hashCode();
        int secondHashCode = second.hashCode();
        assertEquals(first, second);
        assertEquals(firstHashCode, secondHashCode);

    }
    @Test
    void getIdReturnsAccountNumberThroughInterface() {
        AccountNumber number = new AccountNumber("0000000007");
        Identifiable<AccountNumber> entity = new DebitAccount(number, "John", 1000);
        AccountNumber actualId = entity.getId();
        assertEquals(number, actualId);
    }
}