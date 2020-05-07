package cn.utoakto.concurrency.chapter12;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class WaiterThread extends Thread {

    private final BalkingData balkingData;

    public WaiterThread(BalkingData balkingData) {
        super("Waiter");
        this.balkingData = balkingData;
    }

    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            try {
                balkingData.save();
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
