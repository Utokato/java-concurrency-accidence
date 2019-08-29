package cn.llman.concurrency.chapter13;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @date 2019/5/3
 */
public class ProducerThread extends Thread {

    private final MessageQueue queue;
    private final Random random;

    private final static AtomicInteger counter = new AtomicInteger(0);

    public ProducerThread(MessageQueue queue, int seq) {
        super("PRODUCER-" + seq);
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = new Message("Message-" + counter.getAndDecrement());
                queue.put(message);
                System.out.println(Thread.currentThread().getName() + " put: " + message.getData());
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
