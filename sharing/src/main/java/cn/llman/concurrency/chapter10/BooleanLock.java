package cn.llman.concurrency.chapter10;

import java.util.*;

/**
 * 自定义锁
 * 使用一个Boolean 的 flag来实现
 *
 * @date 2019/4/9
 */
public class BooleanLock implements Lock {

    // true 表示该锁已经被锁住; false 表示锁被释放了，可以争抢
    private boolean initValue;

    private Collection<Thread> blockedThreads = new HashSet<>(); // 注意这里的数据结构使用，不能使用 List

    private Thread currentThread;

    public BooleanLock() {
        this.initValue = false;
    }

    @Override
    public synchronized void lock() throws InterruptedException {
        while (initValue) { // 再次注意if和while 的区别
            /**
             * 多线程下，当多个线程第一次由于没有抢到锁而被加入阻塞队列中
             * 有些线程，在被唤醒后，依然没有抢到锁，会重复再次加入到阻塞队列中
             * 所以，这样的逻辑是不对的。
             * 因此，这个阻塞队列需要用Set的数据结构
             */
            blockedThreads.add(Thread.currentThread());
            this.wait();
        }

        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeOutException {
        if (mills <= 0) lock();

        long hasRemaining = mills;
        long endTime = System.currentTimeMillis() + mills;
        while (initValue) {
            if (hasRemaining <= 0) {
                Thread nowCurrent = Thread.currentThread();
                Optional.of(nowCurrent.getName() + " has timed out.")
                        .ifPresent(System.out::println);
                // 当线程等待超时了，需要将超时线程从阻塞队列中移除
                blockedThreads.remove(nowCurrent);
                throw new TimeOutException("Time out");
            }

            blockedThreads.add(Thread.currentThread());
            this.wait(mills);
            hasRemaining = endTime - System.currentTimeMillis();
        }

        blockedThreads.remove(Thread.currentThread());
        this.initValue = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unlock() {
        if (Thread.currentThread() == currentThread) {
            this.initValue = false;
            Optional.of(Thread.currentThread().getName() + " release the lock monitor.")
                    .ifPresent(System.out::println);
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        return Collections.unmodifiableCollection(blockedThreads);
    }

    @Override
    public int getBlockedSize() {
        return blockedThreads.size();
    }
}
