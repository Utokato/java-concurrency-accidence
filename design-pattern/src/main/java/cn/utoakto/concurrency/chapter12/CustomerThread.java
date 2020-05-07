package cn.utoakto.concurrency.chapter12;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class CustomerThread extends Thread {
    private final BalkingData balkingData;
    private final Random random = new Random(System.currentTimeMillis());

    public CustomerThread(BalkingData balkingData) {
        super("Customer");
        this.balkingData = balkingData;
    }

    @Override
    public void run() {
        try {
            balkingData.save();
            for (int i = 0; i < 20; i++) {
                balkingData.change("No." + i);
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                balkingData.save();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
