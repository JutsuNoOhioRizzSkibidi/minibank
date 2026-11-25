package bank.domain;

public abstract class BankAccount implements Identifiable<AccountNumber> {
    private final AccountNumber number;
    private final String owner;
    private double balance;

    protected BankAccount(
            AccountNumber number,
            String owner,
            double initialBalance) {
        if (number == null){
            throw new IllegalArgumentException("счет не может быть null");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("начальный баланс не может быть отрицательным");
        }
            this.number = number;
            this.owner = owner;
            this.balance = initialBalance;
    }
    public synchronized void deposit(double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        balance = balance + amount;
    }
    public abstract void withdraw(double amount);
    public synchronized double getBalance() {return balance;}
    public String getOwner(){ return owner;}
    protected synchronized void withdrawKeepMin(
            double amount,
            double minimumBalance){
        if (amount <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        if (balance - amount < minimumBalance){
            throw new InsufficientFundsException("недостаточно средств");
        }
        balance = balance - amount;
    }
    @Override
    public synchronized String toString(){
        return getClass().getSimpleName() + "{"
                + "number='" + number.value() + '\''
                + ", owner='" + owner + '\''
                + ", balance=" + balance
                + '}';
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (!(obj instanceof BankAccount)){
            return false;
        }
        BankAccount other = (BankAccount) obj;
        return java.util.Objects.equals(this.number, other.number);
    }
    @Override
    public int hashCode(){
        return java.util.Objects.hashCode(number);
    }
    @Override
    public AccountNumber getId(){
        return number;
    }

}
