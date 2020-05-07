package cn.utokato.juc.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/24
 */
public class PhaserExample8 {

    /**
     * {@link Phaser#forceTermination()}
     */
    public static void main(String[] args) throws InterruptedException {

        final Phaser phaser = new Phaser(3);

        new Thread(phaser::arriveAndAwaitAdvance).start();

        TimeUnit.SECONDS.sleep(3);

        System.out.println("isTerminated(): " + phaser.isTerminated());

        phaser.forceTermination();

        System.out.println("isTerminated(): " + phaser.isTerminated());

    }
}
