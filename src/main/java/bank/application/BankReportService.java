
package bank.application;

import bank.domain.AccountNumber;
import bank.domain.BankAccount;
import bank.ports.ReadRepository;

import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;

public class BankReportService{
    private final ReadRepository<AccountNumber, BankAccount> accounts;
    public BankReportService(ReadRepository<AccountNumber, BankAccount> accounts){
        this.accounts = accounts;
    }
    public double totalBalance(){
        return accounts.findAll().stream()
                .mapToDouble(BankAccount::getBalance).sum();
    }
    public OptionalDouble avgBalance(){
        return accounts.findAll().stream()
                .mapToDouble(BankAccount::getBalance).average();
    }
    public Optional<BankAccount> richestAccount(){
        return accounts.findAll().stream()
                .max(Comparator.comparingDouble(BankAccount::getBalance));
    }
}