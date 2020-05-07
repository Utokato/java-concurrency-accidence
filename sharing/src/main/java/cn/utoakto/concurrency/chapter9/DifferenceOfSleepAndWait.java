package cn.utoakto.concurrency.chapter9;

import java.util.stream.Stream;

/**
 * {@link Thread#sleep(long)} 和 {@link Object#wait()} 的区别
 *
 * @date 2019/4/9
 */
public class DifferenceOfSleepAndWait {

    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        // m1();
        // m2();

        Stream.of("T1", "T2").forEach(t -> new Thread(t) {
            @Override
            public void run() {
                // m3();
                m2();
            }
        }.start());
    }

    public static void m1() {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不使用synchronized加锁的话，会抛出IllegalMonitorStateException异常
     */
    public static void m2() {
        synchronized (LOCK) {
            try {
                System.out.println("The thread: " + Thread.currentThread().getName());
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void m3() {
        synchronized (LOCK) {
            try {
                System.out.println("The thread: " + Thread.currentThread().getName());
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
