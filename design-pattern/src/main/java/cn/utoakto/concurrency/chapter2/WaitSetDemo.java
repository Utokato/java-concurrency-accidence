package cn.utoakto.concurrency.chapter2;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @date 2019/4/19
 */
public class WaitSetDemo {

    private static final Object LOCK = new Object();

    private static void work() {
        synchronized (LOCK) {
            System.out.println("Begin...");
            try {
                System.out.println("Thread will coming...");
                LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread will out...");
        }
    }

    @Test
    public void test() throws InterruptedException {
        new Thread(WaitSetDemo::work).start();
        Thread.sleep(1_000);
        synchronized (LOCK) {
            LOCK.notify();
        }
    }


    /**
     * 1. 所有对象都会有一个wait set，用来存放调用了该对象wait方法之后进入blocked状态的线程
     * 2. 调用了该对象的notify方法后，wait set中会有一个线程被唤醒，但该线程不一定会立即得到执行(要再次去争取锁)
     * 3. 从该对象的wait set中一个一个notify其中的线程的话，线程被唤醒的顺序没有保证，不一定是FIFO
     * 4. 线程被唤醒后，必须重新获取锁；获取锁以后，从上次wait的位置开始继续执行，而不是从synchronized锁的第一句代码开始执行
     */
    public static void main(String[] args) throws InterruptedException {

        IntStream.rangeClosed(1, 10).forEach(i -> new Thread(() -> {
            synchronized (LOCK) {
                try {
                    Optional.of(Thread.currentThread().getName() + " will come to WaitSet.").ifPresent(System.out::println);
                    LOCK.wait();
                    Optional.of(Thread.currentThread().getName() + " will leave from WaitSet.").ifPresent(System.out::println);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start());

        Thread.sleep(3000);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            synchronized (LOCK) {
                LOCK.notify();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
