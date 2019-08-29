package cn.llman.concurrency.chapter17;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/7
 */
public class WorkerThread extends Thread {

    private final Channel channel;
    private static final Random random = new Random(System.currentTimeMillis());


    public WorkerThread(String workerName, Channel channel) {
        super(workerName);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            channel.take().execute();
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                // logger
            }
        }
    }
}
