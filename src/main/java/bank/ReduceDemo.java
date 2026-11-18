package bank;

import java.util.List;

public class ReduceDemo {
    public static void main(String[] args) {
        List<BankAccount> accounts = List.of(
                new DebitAccount(
                        new AccountNumber("1234567890"), "sanya1", 1000
                ),
                new DebitAccount(
                        new AccountNumber("1234567899"), "sanya2", 2000
                ),
                new DebitAccount(
                        new AccountNumber("1234567891"), "sanya3", 3000
                )
        );
        double loopTotal = 0;
        for (BankAccount account : accounts) {
            loopTotal += account.getBalance();
        }
        double reduceTotal = accounts.stream()
                .map(BankAccount::getBalance)
                .reduce(0.0, Double::sum);
        System.out.println("через цикл: " + loopTotal);
        System.out.println("через reduce: " + reduceTotal);
    }
}