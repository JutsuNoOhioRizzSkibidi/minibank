package bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionIndex {
    private final Map<AccountNumber, List<Transaction>> byAccount = new HashMap<>();
    public void add(Transaction tx){
        AccountNumber number = tx.account();
        List<Transaction> list = byAccount.get(number);
        if (list == null){
            list = new ArrayList<>();
            byAccount.put(number, list);
        }
        list.add(tx);
    }
    public List<Transaction> findByAccount(AccountNumber number){
        List<Transaction> list = byAccount.get(number);
        if (list == null){
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }
    public void remove(Transaction tx){
        List<Transaction> list = byAccount.get(tx.account());
        if (list == null){
            return;
        }
        list.remove(tx);
        if (list.isEmpty()){
            byAccount.remove(tx.account());
        }
    }
}
