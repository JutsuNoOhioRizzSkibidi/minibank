package bank.domain;

public class FixedCommissionPolicy implements CommissionPolicy {
    private final double commission;
    public FixedCommissionPolicy(double commission){
        if (commission < 0){
            throw new IllegalArgumentException("комиссия не может быть отрицательной");
        }
        this.commission = commission;
    }
    @Override
    public double calculate(double amount){
        return commission;
    }
}