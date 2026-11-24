package bank.adapters.dto;

import bank.domain.AccountNumber;
import bank.domain.Transaction;
import bank.domain.TransactionType;

public record TransactionDto(
        long id,
        String from,
        String to,
        double amount,
        String status
){
    public static TransactionDto fromDomain(Transaction transaction, AccountNumber to){
        if (transaction.type() != TransactionType.TRANSFER){
            throw new IllegalArgumentException("это не транзакция перевода");
        }
        return new TransactionDto(
                transaction.id(),
                transaction.account().value(),
                to.value(),
                transaction.amount(),
                transaction.status().name()
        );
    }
    public static TransactionDto fromDomain(Transaction transaction){
        return new TransactionDto(
                transaction.id(),
                transaction.account().value(),
                "",
                transaction.amount(),
                transaction.status().name()
        );
    }
}