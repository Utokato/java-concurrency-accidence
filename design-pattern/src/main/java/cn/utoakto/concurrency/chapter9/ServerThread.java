package cn.utoakto.concurrency.chapter9;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class ServerThread extends Thread {

    private final RequestQueue queue;
    private final Random random;
    private volatile boolean flag = true;

    public ServerThread(RequestQueue queue) {
        this.queue = queue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (flag) {
            Request request = queue.getRequest();
            if(null==request){
                System.out.println("Received an empty request.");
                continue;
            }
            System.out.println("Server -> " + request.getValue());
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void close() {
        this.flag = false;
        this.interrupt();
    }
}
