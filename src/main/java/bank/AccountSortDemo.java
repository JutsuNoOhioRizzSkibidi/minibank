package bank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AccountSortDemo {
    public static void main (String[] args){
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new DebitAccount(
                new AccountNumber("1234567890"), "Sanya", 5000
        ));
        accounts.add(new DebitAccount(
                new AccountNumber("1234567891"), "Santa", 2000
        ));
        accounts.add(new DebitAccount(
                new AccountNumber("1234567892"), "Santonio", 1000
        ));
        /* анонимный класс
        Comparator<BankAccount> byBalance =
                new Comparator<BankAccount>() {
                    @Override
                    public int compare(BankAccount o1, BankAccount o2) {
                        return Double.compare(
                                o1.getBalance(), o2.getBalance()
                        );
                    }
                };

         */
        /* лямда
        Comparator<BankAccount> byBalance =
                (a , b) -> Double.compare(a.getBalance(), b.getBalance());

         */
        Comparator<BankAccount> byBalance = Comparator.comparingDouble(BankAccount::getBalance);
        accounts.sort(byBalance);
        for (BankAccount account : accounts){
            System.out.println(account);
        }
    }
}
