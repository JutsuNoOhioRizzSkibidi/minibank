package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TransactionJournal {
    private final List<Transaction> transactions = new ArrayList<>();
    private final TransactionIndex index = new TransactionIndex();
    public void add(Transaction transaction){
        if (transaction == null || transaction.account() == null){
            throw new IllegalArgumentException("транзакция и номер счета не может быть null");
        }
        transactions.add(transaction);
        index.add(transaction);
    }
    public List<Transaction> findAll(){
        return new ArrayList<>(transactions);
    }
    public int size(){
        return transactions.size();
    }
    public void removeRejected() {
        Iterator<Transaction> iterator = transactions.iterator();
        while (iterator.hasNext()) {
            Transaction tx = iterator.next();
            if (tx.status() == TransactionStatus.REJECTED) {
                iterator.remove();
                index.remove(tx);
            }
        }
    }
    public List<Transaction> findByAccount(AccountNumber number) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.account().equals(number)) {
                result.add(tx);
            }
        }
        return result;
    }
    public List<Transaction> findByAccountIndex(AccountNumber number){
        return index.findByAccount(number);
    }
}
