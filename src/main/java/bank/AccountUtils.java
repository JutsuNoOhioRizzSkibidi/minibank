package bank;

import java.util.List;
import java.util.Comparator;

public final class AccountUtils {
    public static double totalBalance(
            List<? extends BankAccount> accounts){
        return accounts.stream().mapToDouble(BankAccount::getBalance).sum();
    }
    public static void addDemoDebitAccounts(
            List<? super DebitAccount> target){
        target.add(new DebitAccount(
                new AccountNumber("0000000001"), "Dima", 1000)
        );
        target.add(new DebitAccount(
                new AccountNumber("0000000002"), "Tima", 2000)
        );
    }
    public static <T> void copy(
            List<? extends T> source,
            List<? super T> target){
        for (T element : source){
            target.add(element);
        }
    }
    public static <T extends BankAccount> T richest(List<T> accounts) {
        return accounts.stream().max(Comparator.comparingDouble(BankAccount::getBalance))
                .orElseThrow(() -> new IllegalArgumentException("пустой"));
    }
}
