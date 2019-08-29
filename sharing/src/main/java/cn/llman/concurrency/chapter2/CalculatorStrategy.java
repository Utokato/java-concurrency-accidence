package cn.llman.concurrency.chapter2;

/**
 * 通过这个接口来理解为什么需要{@link Runnable}接口
 *
 * @date 2019/4/7
 */
@FunctionalInterface
public interface CalculatorStrategy {
    double calculate(double salary, double bonus);
}
