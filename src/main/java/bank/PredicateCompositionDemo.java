package bank;

import java.util.List;
import java.util.function.Predicate;

public class PredicateCompositionDemo {
    public static void main(String[] args){
        BlockedAccountRegistry blockedAccounts=
                new BlockedAccountRegistry();
        TransferService transferService = new TransferService(
                amount -> 0.0,
                System.out::println,
                blockedAccounts,
                new TransactionJournal()
        );
        BankService bank = new BankService(
                new InMemoryRepository<>(),
                transferService
        );
        BankAccount first = new DebitAccount(
                new AccountNumber("1234567890"), "sanya", 150_000
        );
        BankAccount second = new DebitAccount(
                new AccountNumber("1234567891"), "alex", 200_000
        );
        BankAccount third = new CreditAccount(
                new AccountNumber("1234567892"), "kayaa", 5000, 10_000
        );
        BankAccount fourth = new SavingsAccount(
                new AccountNumber("1234567893"), "lolka", 120_000, 1000
        );

        bank.openAccount(first);
        bank.openAccount(second);
        bank.openAccount(third);
        bank.openAccount(fourth);
        blockedAccounts.block(second.getId());
        double minBalance = 100_000;
        Predicate<BankAccount> rich =
                a -> a.getBalance() >= minBalance;
        Predicate<BankAccount> notBlocked =
                a -> !blockedAccounts.isBlocked(a.getId());
        Predicate<BankAccount> debit =
                DebitAccount.class::isInstance;
        Predicate<BankAccount> credit =
                CreditAccount.class::isInstance;

        System.out.println("баланс не меньше задан суммы:");
        System.out.println(bank.findAccounts(rich));
        System.out.println("незаблокир счета:");
        System.out.println(bank.findAccounts(notBlocked));
        System.out.println("только дебетовые счета;");
        System.out.println(bank.findAccounts(debit));
        System.out.println("богатые и незблокиров");
        System.out.println(bank.findAccounts(rich.and(notBlocked)));
        System.out.println("недебеотвые счета");
        System.out.println(bank.findAccounts(debit.negate()));
        System.out.println("богатые или кредитные");
        System.out.println(bank.findAccounts(rich.or(credit)));

        Predicate<BankAccount> candidates =
                rich.and(notBlocked).and(debit);
        List<BankAccount> result = bank.findAccounts(candidates);
        System.out.println("богатые незаблокир дебетовые счета:");
        System.out.println(result);
    }
}
