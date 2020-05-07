package cn.utoakto.concurrency.chapter2;

/**
 * @date 2019/4/7
 */
public class TaxCalculator {

    private final double salary;
    private final double bonus;

    private final CalculatorStrategy calculatorStrategy;


    public TaxCalculator(double salary, double bonus, CalculatorStrategy calculatorStrategy) {
        this.salary = salary;
        this.bonus = bonus;
        this.calculatorStrategy = calculatorStrategy;
    }

    public double calculate() {
        return this.calcTax();
    }

    protected double calcTax() {
        return calculatorStrategy.calculate(salary, bonus);
    }

    public CalculatorStrategy getCalculatorStrategy() {
        return calculatorStrategy;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

}
