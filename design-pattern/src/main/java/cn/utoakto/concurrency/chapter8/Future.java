package cn.utoakto.concurrency.chapter8;

/**
 * @date 2019/5/3
 */
public interface Future<T> {

    T get() throws InterruptedException;
}
