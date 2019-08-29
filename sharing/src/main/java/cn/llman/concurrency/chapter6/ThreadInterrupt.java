package cn.llman.concurrency.chapter6;

import org.junit.Test;

import java.awt.datatransfer.Clipboard;

/**
 * @date 2019/4/8
 */
public class ThreadInterrupt {

    private static final Object MONITOR = new Object();

    /**
     * 测试线程在sleep时被打断
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Get a signal of interrupt");
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        Thread.sleep(1000);
        System.out.println(t1.isInterrupted());
        t1.interrupt(); // 中断该线程
        System.out.println(t1.isInterrupted());

    }

    /**
     * 测试线程在wait时被打断
     */
    @Test
    public void testInterrupt() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (MONITOR) {
                    try {
                        MONITOR.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println(Thread.interrupted());
                    }
                }
            }
        });

        t1.start();
        Thread.sleep(1000);
        System.out.println(t1.isInterrupted());
        t1.interrupt(); // 中断该线程
        System.out.println(t1.isInterrupted());
    }

    /**
     * 测试线程在join时被打断
     */
    @Test
    public void testInterrupt1() throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {

                }
            }
        };

        t1.start();
        Thread main = Thread.currentThread();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // t1.interrupt();
                main.interrupt(); // main线程可以被打断
                System.out.println("Interrupt t1 in t2");
            }
        };
        t2.start();

        // 注意join 这个方法的含义
        // 分析，join方法执行后，哪个线程进入了"阻塞"状态
        // 当t1.join()时，代表了只有当t1线程的任务执行完毕后，main线程才会继续执行
        // 所以，t1.join()后，main线程就进入了临时的"阻塞"状态
        // 线程的interrupt方法，打断的都是线程的等待或阻塞状态，如sleep、wait和join
        // 在t1.join()后，由于main线程进入了等待的状态，所以需要被打断是main线程，而不是t1线程
        t1.join();

    }
}
