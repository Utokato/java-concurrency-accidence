package cn.llman.juc.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/23
 */
public class PhaserExample2 {
    private final static Random random = new Random(System.currentTimeMillis());

    /**
     * running
     * <p>
     * bicycle
     * <p>
     * long jump
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        for (int i = 1; i < 6; i++) {
            new SportsMan(phaser, i).start();
        }

    }

    static class SportsMan extends Thread {
        private final Phaser phaser;
        private final int num;

        SportsMan(Phaser phaser, int num) {
            this.phaser = phaser;
            this.num = num;
        }

        @Override
        public void run() {
            try {
                System.out.println("Sportsman: " + num + " start to run.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println("Sportsman: " + num + " end running.");
                System.out.println("getPhase=>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();


                System.out.println("Sportsman: " + num + " start to bicycle.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println("Sportsman: " + num + " end bicycle.");
                System.out.println("getPhase=>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();


                System.out.println("Sportsman: " + num + " start to long jump.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println("Sportsman: " + num + " end long jump.");
                System.out.println("getPhase=>" + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
