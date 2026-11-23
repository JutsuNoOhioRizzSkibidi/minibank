package bank.application;

import bank.ports.Repository;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;

import java.util.Optional;

public class AccountService{
    private final Repository<AccountNumber, BankAccount> accounts;
    public AccountService(Repository<AccountNumber, BankAccount> accounts){
        this.accounts = accounts;
    }
    public void open(BankAccount account){
        if (account == null){
            throw new IllegalArgumentException("счет не может быть нулл");
        }
        accounts.save(account);
    }
    public Optional<BankAccount> find(AccountNumber number){
        return Optional.ofNullable(accounts.findById(number));
    }
    public boolean close(AccountNumber number){
        return accounts.deleteById(number);
    }
}