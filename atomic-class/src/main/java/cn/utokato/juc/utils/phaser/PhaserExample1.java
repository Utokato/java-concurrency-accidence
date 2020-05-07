package cn.utokato.juc.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/23
 */
public class PhaserExample1 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser();

        IntStream.rangeClosed(0, 4).forEach(i -> {
            TaskThread t = new TaskThread(phaser);
            t.start();
        });

        phaser.register();

        phaser.arriveAndAwaitAdvance();
        System.out.println("All of worker finished the task.");

    }

    static class TaskThread extends Thread {
        private final Phaser phaser;

        public TaskThread(Phaser phaser) {
            this.phaser = phaser;
            this.phaser.register();
        }

        @Override
        public void run() {
            System.out.println("The work [ " + getName() + " ] is working...");
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndAwaitAdvance();

        }
    }
}
