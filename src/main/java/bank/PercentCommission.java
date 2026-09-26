package bank;

public class PercentCommission implements CommissionPolicy{
    private final double percent;
    public PercentCommission(double percent){
        if (percent < 0){
            throw new IllegalArgumentException("процент не может быть отриц");
        }
        this.percent = percent;
    }

    @Override
    public double calculate(double amount) {
        return amount * percent / 100;
    }
}
