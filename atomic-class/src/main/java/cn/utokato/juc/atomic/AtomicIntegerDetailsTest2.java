package cn.utokato.juc.atomic;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/16
 */
public class AtomicIntegerDetailsTest2 {

    private final static CompareAndSetLock lock = new CompareAndSetLock();

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    doSomethingWithLightLock();
                } catch (InterruptedException | GetLockFailException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    private static void doSomething() throws InterruptedException {
        synchronized (AtomicIntegerDetailsTest2.class) {
            System.out.println(Thread.currentThread().getName() + " get the lock!");
            TimeUnit.SECONDS.sleep(10);
        }
    }

    private static void doSomethingWithLightLock() throws InterruptedException, GetLockFailException {
        try {
            lock.tryGetLock();
            System.out.println(Thread.currentThread().getName() + " get the lock!");
            TimeUnit.SECONDS.sleep(10);
        } finally {
            lock.releaseLock();
        }
    }
}
