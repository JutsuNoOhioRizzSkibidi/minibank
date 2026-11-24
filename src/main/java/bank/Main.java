package bank;

import bank.domain.AccountNumber;
import bank.domain.Transaction;
import bank.domain.TransactionStatus;
import bank.domain.TransactionType;
import bank.adapters.TransactionFileImporter;
import bank.adapters.dto.TransactionDto;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import bank.adapters.JsonStorage;
import bank.adapters.dto.AccountDto;
import bank.domain.AccountNumber;
import bank.domain.DebitAccount;


public class Main {
    public static void main(String[] args) throws IOException{
        TransactionFileImporter importer = new TransactionFileImporter();

        List<TransactionDto> transactions = importer.load(
                Path.of("transactions.txt")
        );
        for (TransactionDto dto : transactions){
            System.out.println(dto.id() + " " + dto.from() + " " +
                    dto.amount() + " " + dto.status());
        }
        long count = importer.countSuccessful(
                Path.of("transactions.txt")
        );
        System.out.println("успешных транзакций: " + count);
        JsonStorage storage = new JsonStorage();

        List<AccountDto> accounts = List.of(
                AccountDto.fromDomain(new DebitAccount(
                        new AccountNumber("1234567890"), "sanya", 10000
                )),
                AccountDto.fromDomain(new DebitAccount(
                        new AccountNumber("1234567891"), "sanya1", 2000
                ))
        );
        storage.saveAccounts(Path.of("reports", "accounts.json"), accounts);
        System.out.println("счета сохранены в JSON");
    }
}