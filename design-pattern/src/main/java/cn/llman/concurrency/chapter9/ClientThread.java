package cn.llman.concurrency.chapter9;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class ClientThread extends Thread {
    private final RequestQueue queue;
    private final Random random;
    private final String toSendValue;

    public ClientThread(RequestQueue queue, String toSendValue) {
        this.queue = queue;
        this.toSendValue = toSendValue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Client -> request: " + toSendValue);
            queue.putRequest(new Request(toSendValue));
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
