package cn.llman.concurrency.chapter8;

import java.util.concurrent.TimeUnit;

/**
 * @author lma
 * @date 2019/08/27
 */
public class DeadLock2 {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    public void m1() throws Exception {
        synchronized (lock1) {
            TimeUnit.SECONDS.sleep(2);
            synchronized (lock2) { System.out.println("m1, invoked"); }
        }
    }
    public void m2() throws Exception {
        synchronized (lock2) {
            TimeUnit.SECONDS.sleep(2);
            synchronized (lock1) { System.out.println("m2, invoked"); }
        }
    }
    public static void main(String[] args) {
        DeadLock2 demo = new DeadLock2();
        new Thread(() -> {
            try {
                demo.m1();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                demo.m2();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }).start();
    }
}
