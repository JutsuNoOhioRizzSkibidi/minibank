package bank;

public class ProgressiveCommissionPolicy implements CommissionPolicy{
    @Override
    public double calculate(double amount){
        if (amount > 10000){
            return amount * 0.01;
        }
        return amount * 0.005;
    }
}