package cn.llman.juc.atomic;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger 是如何保证了并发编程的3的要素：
 * 首先内部内置了一个 volatile 修饰的 value
 * 所有的操作都是在该value的基础上完成的；
 * volatile 保证了内存的可见性和顺序性，却无法保证原子性
 * 通过内部的CAS算法，compareAndSet，来保证操作的原子性
 * CAS算法，也就是CPU级别的同步指令，相当于乐观锁，它可以探测到其他线程对共享数据的变化情况
 *
 * @date 2019/5/12
 */
public class AtomicIntegerTest {

    /**
     * 保证可见性、顺序性
     * 无法保证原子性
     */
    private static volatile int value = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            int x = 0;
            while (x < 500) {
                int temp = value;
                System.out.println(Thread.currentThread().getName() + ": " + temp);
                value += 1;
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x++;
                /**
                 * value = value + 1;
                 * 1. get value from main memory to local memory
                 * 2. add 1 => x
                 * 3. assign the value to x
                 * 4. flush to main memory
                 */
            }
        });

        Thread t2 = new Thread(() -> {
            int x = 0;
            while (x < 501) {
                int temp = value;
                System.out.println(Thread.currentThread().getName() + ": " + temp);
                value += 1;
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    /**
     * AtomicInteger
     * 保证了并发编程中的：可见性，有序性，原子性
     */
    @Test
    public void testAtomicInteger() throws InterruptedException {
        final AtomicInteger value = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            int x = 0;
            while (x < 500) {
                int temp = value.getAndDecrement();
                System.out.println(Thread.currentThread().getName() + ": " + temp);
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x++;
            }
        });

        Thread t2 = new Thread(() -> {
            int x = 0;
            while (x < 501) {
                int temp = value.getAndDecrement();
                System.out.println(Thread.currentThread().getName() + ": " + temp);
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x++;
            }
        });

        t1.start();
        t2.start();
        t2.join();
        t2.join();
    }

}
