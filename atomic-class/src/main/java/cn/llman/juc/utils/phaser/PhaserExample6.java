package cn.llman.juc.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/24
 */
public class PhaserExample6 {

    /**
     * {@link java.util.concurrent.Phaser#awaitAdvance(int)}
     * 不会参与到getArrivedParties或者getUnarrivedParties的计算
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(7);
       /* new Thread(() -> {
            phaser.awaitAdvance(phaser.getPhase());
        }).start();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(phaser.getArrivedParties());*/

        // 与given phase value(getPhase())相等时，阻塞；不相等时，不阻塞；负数时，phaser terminated
        /*phaser.awaitAdvance(phaser.getPhase());
        System.out.println("=====================");*/

        IntStream.rangeClosed(0, 5).forEach(i -> new AwaitAdvanceTask(phaser));

        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("=======================");


    }

    private static class AwaitAdvanceTask extends Thread {
        private final Phaser phaser;

        private AwaitAdvanceTask(Phaser phaser) {
            this.phaser = phaser;
            start();
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // phaser.arriveAndAwaitAdvance();
            phaser.arrive();
            System.out.println(getName() + " finished work!");
        }
    }
}
