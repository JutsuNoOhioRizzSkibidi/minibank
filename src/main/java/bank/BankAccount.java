package bank;

public abstract class BankAccount{
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
    public void deposit(double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        balance = balance + amount;
    }
    public abstract void withdraw(double amount);
    public double getBalance() {
        return balance;
    }
    protected void withdrawKeepMin(
            double amount,
            double minimumBalance){
        if (amount <= 0){
            throw new IllegalArgumentException("сумма должна быть положительной");
        }
        if (balance - amount < minimumBalance){
            throw new IllegalArgumentException("недостаточно средств");
        }
        balance = balance - amount;
    }
    @Override
    public String toString(){
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
}
