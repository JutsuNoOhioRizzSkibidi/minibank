package bank.domain;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekendCommissionPolicy implements CommissionPolicy {
    private final Clock clock;

    public WeekendCommissionPolicy(Clock clock){
        this.clock = clock;
    }
    @Override
    public double calculate(double amount){
        DayOfWeek day = LocalDate.now(clock).getDayOfWeek();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            return amount * 0.02;
        }
        return amount * 0.01;
    }
}