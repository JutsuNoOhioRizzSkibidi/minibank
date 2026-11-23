package bank.application;

import bank.ports.Repository;
import bank.domain.AccountNumber;
import bank.domain.BankAccount;

public class TransferApplicationService{
    private final Repository<AccountNumber, BankAccount> accounts;
    private final TransferService transferService;

    public TransferApplicationService(
            Repository<AccountNumber, BankAccount> accounts,
            TransferService transferService){
        this.accounts = accounts;
        this.transferService = transferService;
    }
    public void transfer(
            AccountNumber fromNumber, AccountNumber toNumber, double amount
    ){
        BankAccount from = accounts.findById(fromNumber);
        BankAccount to = accounts.findById(toNumber);
        if (from == null){
            throw new IllegalArgumentException("счет отправителя не найден");
        }
        if (to == null){
            throw new IllegalArgumentException("счет получателя не найден");
        }
        transferService.transfer(from, to, amount);
    }
}