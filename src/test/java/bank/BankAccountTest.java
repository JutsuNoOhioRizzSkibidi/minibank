package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BankAccountTest {
    @Test
    void sameNumberAccountsAreEqualByDifType() {
        BankAccount debit =
                new DebitAccount("001", "Иван", 1000);
        BankAccount savings =
                new SavingsAccount("001", "Анна", 2000, 500);
        BankAccount credit =
                new CreditAccount("001", "Олег", 3000, 5000);
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
                new DebitAccount("001", "Иван", 1000);
        BankAccount second =
                new DebitAccount("002", "Иван", 1000);
        boolean result = first.equals(second);
        assertFalse(result);
    }
    @Test
    void balanceChangeDontChangeHashCode(){
        BankAccount first =
                new DebitAccount("003", "Aleksey", 1000);
        BankAccount second =
                new DebitAccount("003", "Aleksey", 1000);
        int originalHashCode = first.hashCode();
        first.deposit(500);
        assertEquals(1500, first.getBalance(), 0.01);
        assertEquals(first, second);
        assertEquals(originalHashCode, first.hashCode());
        assertEquals(first.hashCode(), second.hashCode());
    }
}