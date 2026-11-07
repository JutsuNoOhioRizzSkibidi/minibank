package bank;

import java.util.ArrayList;
import java.util.List;

public class StreamDemo {
    public static void main(String[] args){
        List<BankAccount> accounts = List.of(
                new DebitAccount(new AccountNumber
                        ("1234567890"), "Alex", 50000),
                new DebitAccount(new AccountNumber
                        ("1234567899"), "Sanya", 100000),
                new DebitAccount(new AccountNumber
                        ("1234567891"), "Sanya2", 150000)
        );
        List<BankAccount> loopResult = new ArrayList<>();
        for (BankAccount account : accounts){
            if (account.getBalance() >= 100_000){
                loopResult.add(account);
            }
        }
        List<BankAccount> streamResult = accounts.stream()
                .filter(account -> account.getBalance() >= 100_000).toList();
        System.out.println("результат цикла:");
        loopResult.forEach(System.out::println);
        System.out.println("результат стрим апи:");
        streamResult.forEach(System.out::println);
        System.out.println("результаты совпадают: " + loopResult.equals(streamResult));
    }
}
