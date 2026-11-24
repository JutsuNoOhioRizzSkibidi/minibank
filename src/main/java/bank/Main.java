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
    }
}