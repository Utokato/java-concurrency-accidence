package cn.llman.juc.utils.countDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * {@link CountDownLatch} 在构造的时候需要一个初始值，必须为非负整数
 * 当为负数时，会抛出异常
 * 当为零时，{@link CountDownLatch#await()}方法便不会生效
 * 这是由于await()方法就是判断当前的标示量是否为0，如果为0就不再继续等待
 *
 * @date 2019/5/19
 */
public class CountDownLatchExample3 {
    public static void main(String[] args) throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(1);
        final Thread currentThread = Thread.currentThread();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // latch.countDown(); // countDown方法不被执行，标示量不会减小，所以整个程序会陷入一直阻塞
            currentThread.interrupt(); // interrupt方法也可以中断当前线程的阻塞状态
        }).start();

        // 传入等待时间后，如果在这个时间内标示量减为0了，会直接结束 // 如果在这个时间结束后，不论标示量是否为0，都会结束
        latch.await(1000, TimeUnit.MILLISECONDS);
        System.out.println("===============");
        latch.countDown(); // 当标示量已经为0时，再次调用countDown方法，不会有任何作用
    }
}
