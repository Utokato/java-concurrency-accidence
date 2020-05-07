package cn.utoakto.concurrency.chapter15;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Thread-Per-Message Design Pattern
 *
 * @date 2019/5/5
 */
public class MessageHandler {

    private final static Random random = new Random(System.currentTimeMillis());

    public void request(Message message) {
        new Thread(() -> {
            String msg = message.getValue();
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                System.out.println("The message will be handled by " + Thread.currentThread() + ", content is: " + msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private final static Executor executor = Executors.newFixedThreadPool(5);

    public void requestByThreadPool(Message message) {
        executor.execute(() -> {
            String msg = message.getValue();
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                System.out.println("The message will be handled by " + Thread.currentThread() + ", content is: " + msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void shotdown() {
        ((ExecutorService) executor).shutdown();
    }
}
