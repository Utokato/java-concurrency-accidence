package cn.utokato.juc.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/24
 */
public class PhaserExample5 {

    private final static Random random = new Random(System.currentTimeMillis());

    /**
     * {@link Phaser#arrive()}
     */
    public static void main(String[] args) {
        /*final Phaser phaser = new Phaser(3);
        new Thread(phaser::arrive).start();*/

        final Phaser phaser = new Phaser(5);
        for (int i = 0; i < 4; i++) {
            new ArriveTask(phaser, i).start();
        }
        phaser.arriveAndAwaitAdvance();
        System.out.println("All work is finished in phase one.");
    }

    private static class ArriveTask extends Thread {
        private final Phaser phaser;

        private ArriveTask(Phaser phaser, int num) {
            super(String.valueOf(num));
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(getName() + " start to work.");
            nap();
            System.out.println(getName() + " The phase one is running.");
            phaser.arrive();
            nap();
            System.out.println(getName() + " keep to do other things.");
        }
    }

    private static void nap() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
