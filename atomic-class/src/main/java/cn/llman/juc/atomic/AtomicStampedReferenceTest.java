package cn.llman.juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @date 2019/5/16
 */
public class AtomicStampedReferenceTest {

    private static AtomicStampedReference<Integer> asr = new AtomicStampedReference<>(100, 0);


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                boolean success = asr.compareAndSet(100, 101, asr.getStamp(), asr.getStamp() + 1);
                System.out.println(success);
                success = asr.compareAndSet(101, 100, asr.getStamp(), asr.getStamp() + 1);
                System.out.println(success);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                int stamp = asr.getStamp();
                System.out.println("Before sleep: stamp = " + stamp);
                TimeUnit.SECONDS.sleep(2);
                boolean success = asr.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println(success);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("------------------");
        System.out.println("Main result = " + asr.getReference());

    }
}
