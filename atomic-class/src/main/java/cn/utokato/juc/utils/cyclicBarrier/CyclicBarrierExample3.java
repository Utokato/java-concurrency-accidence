package cn.utokato.juc.utils.cyclicBarrier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/20
 */
public class CyclicBarrierExample3 {

    static class MyCountDownLatch extends CountDownLatch {
        private final Runnable runnable;

        public MyCountDownLatch(int count, Runnable runnable) {
            super(count);
            this.runnable = runnable;
        }

        @Override
        public void countDown() {
            super.countDown();
            if (getCount() == 0) {
                this.runnable.run();
            }
        }
    }

    public static void main(String[] args) {

        final MyCountDownLatch latch = new MyCountDownLatch(2, () -> {
            System.out.println("All threads finished!");
        });

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + " finished!");
        }, "T1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + " finished!");
        }, "T2").start();
    }
}
