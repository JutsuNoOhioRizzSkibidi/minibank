package bank;

import bank.adapters.InMemoryRepository;
import bank.application.BankReportService;
import bank.domain.*;
import bank.ports.ReadRepository;
import bank.ports.Repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ISPTest{
    @Test
    void reportUsesOnlyReadRepository(){
        Repository<AccountNumber, BankAccount> repository = new InMemoryRepository<>();

        repository.save(new DebitAccount(
                new AccountNumber("1234567890"), "sanya", 1000));
        repository.save(new DebitAccount(
                new AccountNumber("1234567891"), "sanya1", 2000));

        ReadRepository<AccountNumber, BankAccount> accounts = repository;
        BankReportService report = new BankReportService(accounts);

        assertEquals(3000, report.totalBalance(), 0.01);
    }
}