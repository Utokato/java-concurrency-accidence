package cn.llman.juc.utils.locks;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @date 2019/5/22
 */
public class ReentrantLockExample {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        // IntStream.rangeClosed(0, 1).forEach(i -> new Thread(ReentrantLockExample::needLock).start());

        Thread t1 = new Thread(ReentrantLockExample::testInterruptedLock);
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        Thread t2 = new Thread(ReentrantLockExample::testInterruptedLock);
        t2.start();
        TimeUnit.SECONDS.sleep(1);

        Optional.of(lock.getQueueLength()).ifPresent(System.out::println); // 判断进入阻塞队列中，阻塞线程的数量
        Optional.of(lock.hasQueuedThreads()).ifPresent(System.out::println); // 判断当前的阻塞队列中，是否有被阻塞住的线程
        Optional.of(lock.hasQueuedThread(t1)).ifPresent(System.out::println); // 判断阻塞队列中，是否有某个线程
        Optional.of(lock.hasQueuedThread(t2)).ifPresent(System.out::println); //


    }

    public static void testTryLock() {
        if (lock.tryLock()) {
            // 已经获取锁了
            try {
                Optional.of(Thread.currentThread().getName() + ", I get a lock.").ifPresent(System.out::println);
            } finally {
                lock.unlock();
            }
        } else {
            // 没有获取到锁
            Optional.of(Thread.currentThread().getName() + ", I don't get a lock.").ifPresent(System.out::println);
        }
    }

    public static void testInterruptedLock() {
        try {
            lock.lockInterruptibly(); // 如果某线程由于获取该锁而陷入了阻塞，可以通过interrupt方法区打断该线程的阻塞状态
            Optional.of(Thread.currentThread().getName() + ":" + lock.getHoldCount()) // 判断当前锁保护的线程，如果该线程被该锁保护，返回1
                    .ifPresent(System.out::println);
            Optional.of("The thread " + Thread.currentThread().getName() + " get the lock, and start working.")
                    .ifPresent(System.out::println);
            while (true) {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void needLock() {
        try {
            lock.lock();
            Optional.of("The thread " + Thread.currentThread().getName() + " get the lock, and start working.")
                    .ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void needLockBySync() {
        synchronized (ReentrantLockExample.class) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
