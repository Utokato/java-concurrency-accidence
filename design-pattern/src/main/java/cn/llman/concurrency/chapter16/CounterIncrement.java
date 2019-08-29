package cn.llman.concurrency.chapter16;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Two-Phase Termination Design Pattern
 *
 * @date 2019/5/5
 */
public class CounterIncrement extends Thread {

    private volatile boolean terminated = false;

    private int counter = 0;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public void run() {
        try {
            while (!terminated) {
                System.out.println(Thread.currentThread().getName() + " " + counter++);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            // e.printStackTrace();
        } finally {
            // second phase
            this.clean();
        }

    }

    private void clean() {
        System.out.println("do some clean work for this thread in second phase, and current counter is " + counter);
    }


    public void close() {
        this.terminated = true;
        // System.out.println(this);
        this.interrupt();
    }
}
