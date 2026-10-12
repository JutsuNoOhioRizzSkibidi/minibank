package bank;

import java.util.List;
public final class AccountUtils {
    public static double totalBalance(
            List<? extends BankAccount> accounts){
        double total = 0;
        for (BankAccount account : accounts){
            total += account.getBalance();
        }
        return total;
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
        if (accounts.isEmpty()) {
            throw new IllegalArgumentException("список счетов не должен быть пустым");
        }
        T richestAccount = accounts.get(0);
        for (int i = 1; i < accounts.size(); i++) {
            T current = accounts.get(i);
            if (current.getBalance() > richestAccount.getBalance()) {
                richestAccount = current;
            }
        }
        return richestAccount;
    }
}
