package cn.utokato.juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @date 2019/5/16
 */
public class AtomicBooleanFlag {
    private final static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (flag.get()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("I am working");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("I am Finished!");
        }).start();

        TimeUnit.SECONDS.sleep(5);

        flag.set(false);

    }


}
