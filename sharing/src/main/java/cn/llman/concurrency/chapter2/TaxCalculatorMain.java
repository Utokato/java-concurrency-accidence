package cn.llman.concurrency.chapter2;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @date 2019/4/7
 */
public class TaxCalculatorMain {

    public static void main(String[] args) {
        /*TaxCalculator taxCalculator = new TaxCalculator(10000d, 2000d) {
            @Override
            protected double calcTax() {
                return getSalary() * 0.1 + getBonus() * 0.15;
            }
        };
        double tax = taxCalculator.calculate();
        System.out.println(tax);*/

        TaxCalculator taxCalculator = new TaxCalculator(10000d, 2000d, (s, b) -> s * 0.1 + b * 0.15);
        double tax = taxCalculator.calculate();
        System.out.println(tax);
    }
}
