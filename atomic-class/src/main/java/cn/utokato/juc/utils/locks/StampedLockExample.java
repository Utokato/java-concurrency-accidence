package cn.utokato.juc.utils.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date 2019/5/23
 */
public class StampedLockExample {


    /**
     * ReentrantLock(可重入锁) VS synchronized(关键字)
     * <p>
     * <p>
     * ReentrantReadWriteLock(可重入读写锁)
     * <p>
     * <p>
     * 情景：
     * 有100个线程，其中有99个需要读取锁，1个需要写入锁
     * 当使用ReentrantReadWriteLock时，需要获取锁才能对共享数据进行操作
     * ReentrantReadWriteLock是一个互斥锁(悲观锁)
     * 由于有多个读取线程，获取读取锁的几率更大，可能会造成写入线程一直抢不到锁
     * 造成了写饥饿的结果
     * <p>
     * 为了解决这个问题，引入了StampedLock
     * StampedLock是一个乐观锁
     * 即读取操作的时候，也可以进行写入操作
     * 但是，读取是会进行判断，是否当前的数据被写入线程修改了
     * 如果没有修改，就直接返回，如果修改了，就重新进行一次读取操作
     * 本质上是CAS算法的扩展
     */
    public static void main(String[] args) {

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        Runnable readTask = () -> {
            for (; ; ) {
                read();
            }
        };
        Runnable writeTask = () -> {
            for (; ; ) {
                write();
            }
        };

        IntStream.rangeClosed(0, 8).forEach(i -> {
            executor.submit(readTask);
        });
        executor.submit(writeTask);
    }

    private final static StampedLock lock = new StampedLock();

    private final static List<Long> data = new ArrayList<>();

    private static void read() { // 悲观的读锁
        long stamped = -1;
        try {
            stamped = lock.readLock();
            Optional.of(
                    data.stream().map(l -> String.valueOf(l).substring(10)).collect(Collectors.joining("#", "R-", ""))
            ).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockRead(stamped);
        }
    }

    private static void write() {
        long stamped = -1;
        try {
            stamped = lock.writeLock();
            data.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamped);
        }
    }
}
