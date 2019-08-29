package cn.llman.juc.atomic;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @date 2019/5/12
 */
public class AtomicIntegerDetailsTest {

    @Test
    public void testAtomicInteger() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger.get());
        atomicInteger = new AtomicInteger(10);
        System.out.println(atomicInteger.get());

        // set
        atomicInteger.set(15);
        System.out.println(atomicInteger.get());
        atomicInteger.lazySet(20);
        System.out.println(atomicInteger);

        System.out.println("----------------------------");

        // getAndAdd
        AtomicInteger getAndAdd = new AtomicInteger(10);
        int result = getAndAdd.getAndAdd(12);
        System.out.println("getAndAdd :" + getAndAdd.get());
        System.out.println("result :" + result);
        System.out.println("-----------------------");

        // 多线程操作同一个原子变量
        AtomicInteger change = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                int v = change.addAndGet(1);
                System.out.println(Thread.currentThread().getName() + ": " + v);
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                int v = change.addAndGet(1);
                System.out.println(Thread.currentThread().getName() + ": " + v);
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * CAS 算法， CompareAndSet
     * 最快失败策略
     */
    @Test
    public void test() {
        AtomicInteger i = new AtomicInteger(10);
        boolean result = i.compareAndSet(12, 20);
        System.out.println(result);
        System.out.println(i);
    }
}
