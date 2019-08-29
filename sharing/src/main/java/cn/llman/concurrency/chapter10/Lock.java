package cn.llman.concurrency.chapter10;

import java.util.Collection;

/**
 * @date 2019/4/9
 */
public interface Lock {

    void lock() throws InterruptedException;

    void lock(long mills) throws InterruptedException, TimeOutException;

    void unlock();

    Collection<Thread> getBlockedThread();

    int getBlockedSize();


    class TimeOutException extends Exception {
        TimeOutException(String message) {
            super(message);
        }
    }
}
