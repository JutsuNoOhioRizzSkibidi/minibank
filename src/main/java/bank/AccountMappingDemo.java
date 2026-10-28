package bank;

import java.util.List;
import java.util.function.Function;

public class AccountMappingDemo {
    public static void main(String[] args){
        List<BankAccount> accounts = List.of(
                new DebitAccount(
                        new AccountNumber("1234567890"), "sanya", 5000
                ),
                new SavingsAccount(
                        new AccountNumber("1234567899"), "Sanya1", 10000, 1000
                )
        );
        Function<BankAccount, AccountSummary> toSummary=
                account -> new AccountSummary(
                        account.getId(),
                        account.getClass().getSimpleName(),
                        account.getBalance()
                );
        List<AccountSummary> summaries =
                CollectionFunctions.map(accounts, toSummary);
        System.out.println("краткие сведения о счетах: ");
        for (AccountSummary summary: summaries){
            System.out.println(summary);
        }
        Function<BankAccount, String> label =
                account -> account.getId().value() + " : " + account.getBalance();
        List<String> labels =
                CollectionFunctions.map(accounts, label);
        System.out.println("строковые представления: ");
        for (String text : labels){
            System.out.println(text);
        }
    }
}
