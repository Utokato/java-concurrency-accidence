package cn.llman.concurrency.chapter3;

import java.util.Arrays;

/**
 * @date 2019/4/7
 */
public class CreateThread2 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        ThreadGroup tg1 = t.getThreadGroup();

        System.out.println(Thread.currentThread().getName());

        ThreadGroup tg2 = Thread.currentThread().getThreadGroup();

        System.out.println(tg1);
        System.out.println(tg2);
        System.out.println(tg1.equals(tg2));


        /* -------------------- */

        int count = tg2.activeCount();
        System.out.println("ActiveCount: " + count);
        Thread[] threads = new Thread[count];
        tg2.enumerate(threads);
        Arrays.stream(threads).forEach(System.out::println);
    }
}
