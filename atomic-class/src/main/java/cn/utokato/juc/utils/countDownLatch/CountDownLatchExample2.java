package cn.utokato.juc.utils.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/19
 */
public class CountDownLatchExample2 {


    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            System.out.println("Do some initial work.");
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                latch.await();
                System.out.println("Do other work.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("Async prepare for some data.");
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println("Date preparation is done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        new Thread(()->{
            try {
                latch.await();
                System.out.println("Release.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
