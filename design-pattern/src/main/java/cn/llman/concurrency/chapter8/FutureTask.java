package cn.llman.concurrency.chapter8;

/**
 * @date 2019/5/3
 */
public interface FutureTask<T> {

    T call() throws InterruptedException;
}
