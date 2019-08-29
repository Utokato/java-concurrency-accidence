package cn.llman.juc.atomic;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date 2019/5/16
 */
public class UnsafeTest {

    /**
     * Java is a safe programming language
     * and prevents programmer from doing a lot of stupid mistakes,
     * most of which based on memory management.
     * But, there is a way to do such mistakes intentionally,
     * using Unsafe class.
     *
     * @param args
     */
    public static void main(String[] args) {

        Unsafe unsafe = Unsafe.getUnsafe();

        System.out.println(unsafe);  // java.lang.SecurityException: Unsafe
    }


    /**
     * 通过反射获取Unsafe类的实例
     */
    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试 Unsafe实例
     */
    @Test
    public void testGetUnsafeInstance() {
        Unsafe unsafe = getUnsafe();
        System.out.println(unsafe);
    }

    interface Counter {
        void increment();

        long getCounter();
    }

    static class CounterRunnable implements Runnable {

        private final Counter counter;
        private final int num;

        CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }

    static class StupidCounter implements Counter {

        private long counter = 0;

        @Override
        public void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class SyncCounter implements Counter {

        private long counter = 0;

        @Override
        public synchronized void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class LockCounter implements Counter {

        private long counter = 0;
        private final Lock lock = new ReentrantLock();

        @Override
        public void increment() {
            try {
                lock.lock();
                counter++;
            } finally {
                lock.unlock();
            }

        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class AtomicCounter implements Counter {

        private AtomicLong counter = new AtomicLong();

        @Override
        public void increment() {
            counter.incrementAndGet();
        }

        @Override
        public long getCounter() {
            return counter.get();
        }
    }

    static class CasCounter implements Counter {

        private volatile long counter = 0;
        private Unsafe unsafe;
        private long offset;

        CasCounter() throws Exception {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }

        @Override
        public void increment() {
            while (!unsafe.compareAndSwapLong(this, offset, counter, counter + 1)) {
                // do nothing
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    /**
     * {@link StupidCounter}
     * -    Counter result: 9972668
     * -    Time passed in ms: 196
     * <p>
     * {@link SyncCounter}
     * -    Counter result: 10000000
     * -    Time passed in ms: 631
     * <p>
     * {@link LockCounter}
     * -    Counter result: 10000000
     * -    Time passed in ms: 481
     * <p>
     * {@link AtomicCounter}
     * -    Counter result: 10000000
     * -    Time passed in ms: 522
     * <p>
     * {@link CasCounter}
     * -    Counter result: 10000000
     * -    Time passed in ms: 668
     */
    @Test
    public void test1() throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(1000);
        Counter counter = new CasCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            threadPool.submit(new CounterRunnable(counter, 10000));
        }
        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms: " + (end - start));
    }


}
