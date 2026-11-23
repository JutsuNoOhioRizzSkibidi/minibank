package bank;

import bank.domain.CommissionPolicy;
import bank.domain.FixedCommissionPolicy;
import bank.domain.ProgressiveCommissionPolicy;
import bank.domain.WeekendCommissionPolicy;
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class CommissionPolicyTest{
    @Test
    void fixedCommissionTest(){
        CommissionPolicy policy = new FixedCommissionPolicy(100);
        assertEquals(100.0, policy.calculate(1000), 0.01);
        assertEquals(100.0, policy.calculate(5000), 0.01);
    }
    @Test
    void progressiveCommissionTest(){
        CommissionPolicy policy = new ProgressiveCommissionPolicy();
        assertEquals(25.0, policy.calculate(5000), 0.01);
        assertEquals(200.0, policy.calculate(20000), 0.01);
    }
    @Test
    void weekendCommissionTest(){
        Clock clock = Clock.fixed(
                Instant.parse("2026-11-22T12:00:00Z"),
                ZoneOffset.UTC
        );
        CommissionPolicy policy = new WeekendCommissionPolicy(clock);
        assertEquals(200.0, policy.calculate(10000), 0.01);
    }
    @Test
    void weekdayCommissionTest(){
        Clock clock = Clock.fixed(
                Instant.parse("2026-11-23T12:00:00Z"),
                ZoneOffset.UTC
        );
        CommissionPolicy policy = new WeekendCommissionPolicy(clock);
        assertEquals(100.0, policy.calculate(10000), 0.01);
    }
}