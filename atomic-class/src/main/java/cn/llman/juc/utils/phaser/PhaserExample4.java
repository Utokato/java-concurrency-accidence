package cn.llman.juc.utils.phaser;

import org.junit.Test;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/24
 */
public class PhaserExample4 {

    public static void main(String[] args) throws InterruptedException {

        final Phaser phaser = new Phaser(1);

        /*System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());

        phaser.arriveAndAwaitAdvance();
        System.out.println(phaser.getPhase());*/

        /*System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());*/

        /*System.out.println(phaser.getArrivedParties());
        System.out.println(phaser.getUnarrivedParties());*/

     /*   phaser.bulkRegister(10);
        System.out.println(phaser.getRegisteredParties());
        System.out.println(phaser.getArrivedParties());
        System.out.println(phaser.getUnarrivedParties());
        new Thread(phaser::arriveAndAwaitAdvance).start();
        System.out.println("================");
        TimeUnit.SECONDS.sleep(1);
        System.out.println(phaser.getRegisteredParties());
        System.out.println(phaser.getArrivedParties());
        System.out.println(phaser.getUnarrivedParties());*/

        final Phaser phaser1 = new Phaser(2) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return false;
            }
        };

        new OnAdvancedTask("Alisa", phaser1).start();
        new OnAdvancedTask("Bob", phaser1).start();

    }

    @Test
    public void test() {

    }

    static class OnAdvancedTask extends Thread {
        private final Phaser phaser;

        OnAdvancedTask(String name, Phaser phaser) {
            super(name);
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(getName() + " I am start. The phase is " + phaser.getPhase());
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + " I am end.");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(getName() + " I am start. The phase is " + phaser.getPhase());
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + " I am end.");
        }
    }
}
