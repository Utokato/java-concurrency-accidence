package cn.utoakto.concurrency.chapter10;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class ThreadLocalComplexTest {

    private final static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "Default value");
    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            threadLocal.set("Thread-T0");
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                System.out.println(Thread.currentThread().getName() + " -> " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            threadLocal.set("Thread-T1");
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                System.out.println(Thread.currentThread().getName() + " -> " + threadLocal.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();

        t1.join();
        t2.join();

        System.out.println("======");
        System.out.println(Thread.currentThread().getName() + " -> " + threadLocal.get());

    }

}
