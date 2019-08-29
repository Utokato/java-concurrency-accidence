package cn.llman.concurrency.chapter3;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Just test by myself
 * <p>
 * {@link Thread#isInterrupted()} 和 {@link Thread#interrupt()} 的 区别
 *
 * @date 2019/4/24
 */
public class ThreadInterruptTest {

    @Test
    public void test1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                System.out.println("I get a signal that I am interrupted!");
                System.out.println("Is " + Thread.currentThread().getName() + " is isInterrupted ? " + Thread.currentThread().isInterrupted());
                System.out.println("Is " + Thread.currentThread().getName() + " is interrupted ? " + Thread.interrupted());
            }
        }, "T1");

        thread.start();
        TimeUnit.MILLISECONDS.sleep(2);

        System.out.println("T1 is interrupted ? " + thread.isInterrupted());
        thread.interrupt();
        System.out.println("T1 is interrupted ? " + thread.isInterrupted());


    }

    public static void main(String[] args) {

        System.out.println("Is main thread interrupted? " + Thread.interrupted());
        System.out.println("Is main thread isInterrupted? " + Thread.currentThread().isInterrupted());


        Thread.currentThread().interrupt();

        // System.out.println("Is main thread interrupted? " + Thread.interrupted());
        System.out.println("Is main thread isInterrupted? " + Thread.currentThread().isInterrupted());

        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            // e.printStackTrace();
            System.out.println("I get a signal that I will be interrupted!");
        }
    }
}
