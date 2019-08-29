package cn.llman.concurrency.chapter10;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019/5/3
 */
public class ThreadLocalSimulator<T> {

    private final Map<Thread, T> storage = new HashMap<>();

    public void set(T t) {
        synchronized (this) {
            Thread key = Thread.currentThread();
            storage.put(key, t);
        }
    }

    public T get() {
        synchronized (this) {
            T value = storage.get(Thread.currentThread());
            if (null == value) {
                return initialValue();
            }
            return value;
        }
    }

    public T initialValue() {
        return null;
    }
}
