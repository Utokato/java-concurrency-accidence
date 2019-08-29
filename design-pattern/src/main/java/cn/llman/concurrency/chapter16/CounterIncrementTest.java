package cn.llman.concurrency.chapter16;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/5
 */
public class CounterIncrementTest {
    public static void main(String[] args) throws InterruptedException {
        CounterIncrement counterIncrement = new CounterIncrement();
        counterIncrement.start();

        TimeUnit.MILLISECONDS.sleep(10000);
        counterIncrement.close();
    }
}
