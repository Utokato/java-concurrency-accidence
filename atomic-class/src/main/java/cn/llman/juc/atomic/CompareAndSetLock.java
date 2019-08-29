package cn.llman.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @date 2019/5/16
 */
public class CompareAndSetLock {
    private final AtomicInteger value = new AtomicInteger(0);

    /**
     * 记录由哪一个线程获取了锁，只有获取到锁的线程才能去释放锁
     */
    private Thread threadWithLock;

    public void tryGetLock() throws GetLockFailException {
        boolean success = value.compareAndSet(0, 1);
        if (!success) {
            throw new GetLockFailException("Get Lock fail Exception !");
        } else {
            this.threadWithLock = Thread.currentThread();
        }
    }

    public void releaseLock() {
        if (0 == value.get()) {
            return;
        }
        /* 判断当前线程是不是获取锁的线程，如果是就释放锁 */
        if (this.threadWithLock.equals(Thread.currentThread())) {
            value.compareAndSet(1, 0);
        }

    }

}
