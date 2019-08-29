package cn.llman.concurrency.chapter14;

import org.junit.Test;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/3
 */
public class CustomCountDownClient {
    private final static CountDown countDown = new CountDown(5);

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        // first phase
        System.out.println("准备进入第一阶段，需要多个线程同时处理任务");
        IntStream.rangeClosed(1, 5).forEach(i -> new Thread(() -> {
            System.out.println("线程" + Thread.currentThread().getName() + "正在工作");
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDown.down();
        }, String.valueOf(i)).start());

        countDown.await();

        // second phase
        System.out.println("多个线程同时处理任务结束，准备开始第二阶段");
        System.out.println("................");
        System.out.println("全部任务处理结束");

    }


    @Test
    public void test() throws InterruptedException {
        for (; ; ) {
            System.out.println("hhh");
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
