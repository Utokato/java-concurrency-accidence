package cn.utoakto.concurrency.chapter13;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class ConsumerThread extends Thread {
    private final MessageQueue queue;
    private final Random random;

    public ConsumerThread(MessageQueue queue, int seq) {
        super("CONSUMER-" + seq);
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = queue.take();
                System.out.println(Thread.currentThread().getName() + " take: " + message.getData());
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
