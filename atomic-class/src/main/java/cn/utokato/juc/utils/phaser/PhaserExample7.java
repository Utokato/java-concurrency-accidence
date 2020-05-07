package cn.utokato.juc.utils.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @date 2019/5/24
 */
public class PhaserExample7 {

    /**
     * {@link Phaser#awaitAdvanceInterruptibly(int, long, TimeUnit)}
     * {@link Phaser#awaitAdvanceInterruptibly(int)}
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(3);
        /*Thread t1 = new Thread(phaser::arriveAndAwaitAdvance);
        t1.start();
        System.out.println("=====================");
        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();
        System.out.println("=========thread interrupted=========");*/

       /* Thread t2 = new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase());
                System.out.println("*****************");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        System.out.println("=====================");
        TimeUnit.SECONDS.sleep(5);
        t2.interrupt();
        System.out.println("=========thread interrupted=========");*/

        Thread t3 = new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 5, TimeUnit.SECONDS);
                System.out.println("*****************");
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        t3.start();
        System.out.println("=====================");
        TimeUnit.SECONDS.sleep(10);
        t3.interrupt();
        System.out.println("=========thread interrupted=========");

    }

}
