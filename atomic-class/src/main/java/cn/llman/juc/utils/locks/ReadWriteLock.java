package cn.llman.juc.utils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @date 2019/5/22
 */
public class ReadWriteLock {

    private final static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final static Lock readLock = readWriteLock.readLock();
    private final static Lock writeLock = readWriteLock.writeLock();

    private final static List<Long> data = new ArrayList<>();


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(ReadWriteLock::write);
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        Thread t2 = new Thread(ReadWriteLock::read);
        t2.start();
        Thread t3 = new Thread(ReadWriteLock::read);
        t3.start();
    }

    private static void write() {
        try {
            writeLock.lock();
            data.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private static void read() {
        try {
            readLock.lock();
            data.forEach(System.out::println);
            System.out.println(Thread.currentThread().getName() + "===================");
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }
}
