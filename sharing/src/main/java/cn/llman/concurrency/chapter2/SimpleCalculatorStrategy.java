package cn.llman.concurrency.chapter2;

/**
 * @date 2019/4/7
 */
public class SimpleCalculatorStrategy implements CalculatorStrategy {

    private final double SALARY_RATE = 0.1;
    private final double BONUS_RATE = 0.15;

    @Override
    public double calculate(double salary, double bonus) {
        return salary * SALARY_RATE + bonus * BONUS_RATE;
    }
}
