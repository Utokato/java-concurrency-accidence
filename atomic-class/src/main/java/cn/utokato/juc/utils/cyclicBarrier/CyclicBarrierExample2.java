package cn.utokato.juc.utils.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/20
 */
public class CyclicBarrierExample2 {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            System.out.println("I am callback function, and I am invoked when all threads reach.");
        });

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(60);
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println(Thread.currentThread().getName() + ", I am interrupted.");
                e.printStackTrace();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println(Thread.currentThread().getName() + ", I am interrupted.");
                e.printStackTrace();
            }
        }, "T2").start();

       /* TimeUnit.SECONDS.sleep(4);
        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());

        TimeUnit.SECONDS.sleep(2);

        // reset == initial == finished
        cyclicBarrier.reset();

        System.out.println(cyclicBarrier.getNumberWaiting());
        System.out.println(cyclicBarrier.getParties());
        System.out.println(cyclicBarrier.isBroken());*/
    }
}
