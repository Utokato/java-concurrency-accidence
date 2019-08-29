package cn.llman.concurrency.chapter3;

import java.util.concurrent.TimeUnit;

/**
 * Just test {@link TimeUnit} by myself
 *
 * @date 2019/4/24
 */
public class TimeUnitTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("to test TimeUnit starting!");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("to test TimeUnit end!");
    }
}
