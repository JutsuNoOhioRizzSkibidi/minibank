package bank;

public class BankService {
    private final Repository<AccountNumber, BankAccount> accounts;
    private final TransferService transferService;
    public BankService(Repository<AccountNumber, BankAccount> accounts,
                       TransferService transferService){
        this.accounts = accounts;
        this.transferService = transferService;
    }
    public void openAccount(BankAccount account){
        if (account == null){
            throw new IllegalArgumentException("счет не может быть нулл");
        }
        accounts.save(account);
    }
    public BankAccount findAccount(AccountNumber number){
        return accounts.findById(number);
    }
    public boolean closeAccount(AccountNumber number){
        return accounts.deleteById(number);
    }
    public int accountCount(){
        return accounts.size();
    }
    public void transfer(
            AccountNumber fromNumber, AccountNumber toNumber, double amount
    ){
        BankAccount from = accounts.findById(fromNumber);
        BankAccount to = accounts.findById(toNumber);
        if(from == null){
            throw new IllegalArgumentException("счет отправителя не найден");
        }
        if (to == null){
            throw new IllegalArgumentException("счет получателя не найден");
        }
        transferService.transfer(from, to, amount);
    }
}
