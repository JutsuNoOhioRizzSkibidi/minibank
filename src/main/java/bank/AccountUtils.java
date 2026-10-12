package bank;

import java.util.List;
public class AccountUtils {
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
}
