package bank.application;

import bank.ports.Repository;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;

import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;

public class BankReportService{
    private final Repository<AccountNumber, BankAccount> accounts;

    public BankReportService(Repository<AccountNumber, BankAccount> accounts){
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