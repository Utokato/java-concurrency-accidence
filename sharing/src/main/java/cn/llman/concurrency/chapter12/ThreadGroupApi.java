package cn.llman.concurrency.chapter12;


import org.junit.Test;

import java.util.Arrays;

/**
 * 类中线程组及线程的包含关系
 * <p>
 * -              main->main,Monitor
 * -                 |
 * -
 * -               tg1->t1
 * -                 |
 * -
 * -               tg2->t2
 *
 * @date 2019/4/10
 */
public class ThreadGroupApi {

    public static void main(String[] args) {
        ThreadGroup tg1 = new ThreadGroup("TG1");
        Thread t1 = new Thread(tg1, "T1") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        // e.printStackTrace();
                        System.out.println("t1 get a interrupted signal");
                        break;
                    }
                }
            }
        };
        t1.start();


        ThreadGroup tg2 = new ThreadGroup(tg1, "TG2");
        Thread t2 = new Thread(tg2, "T2") {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        // e.printStackTrace();
                        System.out.println("t2 get a interrupted signal");
                        break;
                    }
                }
            }
        };
        t2.start();


        System.out.println(tg1.activeCount());
        System.out.println(tg1.activeGroupCount());

        tg1.checkAccess();

        // tg1.destroy(); // 当ThreadGroup中还包含有正在执行的线程，destroy方法会抛出异常

        Thread[] ts1 = new Thread[tg1.activeCount()];
        tg1.enumerate(ts1);
        Arrays.stream(ts1).forEach(System.out::println);

        System.out.println("--------------------");

        Thread[] ts2 = new Thread[5];
        // enumerate中的第二个参数：true表示将该线程组及其子线程组中所有的线程都枚举出来；
        // false表示仅将该线程组内的线程枚举出来
        Thread.currentThread().getThreadGroup().enumerate(ts2, false);
        Arrays.stream(ts2).forEach(System.out::println);

        System.out.println("--------------------");
        tg1.interrupt();


    }

    @Test
    public void testThreadGroupDestroy() throws InterruptedException {
        ThreadGroup tg1 = new ThreadGroup("TG1");
        Thread t1 = new Thread(tg1, "T1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {

                }
            }
        };
        // 线程组setDaemon方法表示了当这个线程组中的所有线程或子线程组中的所有线程都执行完毕后，这个线程组自动处于destroyed状态
        // tg1.setDaemon(true);
        t1.start();
        Thread.sleep(2_000);
        // 手动将该线程组置为destroyed状态，如果该线程组及子线程组中还有线程正在运行，就会抛出IllegalThreadStateException异常
        tg1.destroy();
        System.out.println(tg1.isDestroyed());
    }
}
