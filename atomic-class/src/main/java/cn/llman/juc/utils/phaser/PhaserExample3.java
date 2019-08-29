package cn.llman.juc.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/24
 */
public class PhaserExample3 {
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
        for (int i = 1; i < 5; i++) {
            new SportsMan(phaser, i).start();
        }
        new InjuredSportsMan(phaser, 6).start();

    }

    static class InjuredSportsMan extends Thread {
        private final Phaser phaser;
        private final int num;

        InjuredSportsMan(Phaser phaser, int num) {
            this.phaser = phaser;
            this.num = num;
        }

        @Override
        public void run() {
            try {
                sport(num, phaser, " start to run.", " end running.");
                sport(num, phaser, " start to bicycle.", " end bicycle.");
                // System.out.println("I am injured.");
                System.out.println("I am injured, and I will be exited.");
                phaser.arriveAndDeregister();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
                sport(num, phaser, " start to run.", " end running.");
                sport(num, phaser, " start to bicycle.", " end bicycle.");
                sport(num, phaser, " start to long jump.", " end long jump.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static void sport(int num, Phaser phaser, String s, String s2) throws InterruptedException {
        System.out.println("Sportsman: " + num + s);
        TimeUnit.SECONDS.sleep(random.nextInt(5));
        System.out.println("Sportsman: " + num + s2);
        // System.out.println("getPhase=>" + phaser.getPhase());
        phaser.arriveAndAwaitAdvance();
    }
}
