package cn.utokato.juc.utils.cyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/20
 */
public class CyclicBarrierExample1 {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            System.out.println("All threads had finished, main thread also left from barrier.");
        });

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName() + " finished, and met a barrier.");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " left from barrier.");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + " finished, and met a barrier.");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " left from barrier.");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

        }).start();

       /* cyclicBarrier.await();
        System.out.println("All threads had finished, main thread also left from barrier.");*/

        while (true) {
            System.out.println(cyclicBarrier.getNumberWaiting());
            System.out.println(cyclicBarrier.getParties());
            System.out.println(cyclicBarrier.isBroken());
            TimeUnit.SECONDS.sleep(2);
        }


    }
}
