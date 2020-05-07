package cn.utoakto.concurrency.chapter9;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * 生产消费者模式 版本二
 * 生产者生产一个数据后，便进入等待状态，直到消费者消费了数据以后再去唤醒它
 * 消费者消费一个数据后，便进入等待状态，直到生产者生产了数据以后再去唤醒它
 *
 * @date 2019/4/9
 */
public class ProducerConsumerVersion2 {

    private int i = 0;
    private final Object LOCK = new Object();
    private volatile boolean isProduced = false;


    private void produce() {
        synchronized (LOCK) {
            if (isProduced) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
                System.out.println("Produce data --> " + i + " in a thread named: " + Thread.currentThread().getName());
                // to do notify
                LOCK.notify();
                isProduced = true;
            }
        }
    }

    private void consume() {
        synchronized (LOCK) {
            if (isProduced) {
                System.out.println("Consume data --> " + i + " in a thread named: " + Thread.currentThread().getName());
                // to do notify
                LOCK.notify();
                isProduced = false;
            } else {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerVersion2 pc = new ProducerConsumerVersion2();

        new Thread("P") {
            @Override
            public void run() {
                while (true) {
                    pc.produce();
                }
            }
        }.start();

        new Thread("C") {
            @Override
            public void run() {
                while (true) {
                    pc.consume();
                }
            }
        }.start();
    }

    /**
     * 多个生产者和多个消费者情况下，可能出现假死的状况
     * 主要的原因就是当多个线程进入了wait状态时，当一个线程进行了notify时，不一定唤醒的是哪一个线程
     * 如果所有的线程交替都进入了wait状态，就出现了<b>假死</b>的情况。
     * <p>
     * 比如：
     * 当P1产生了一个数据，它进入了wait状态
     * 此时P2得到了CPU的时间片，它发现已经有数据了，所有也进入wait状态
     * 之后C1得到了CPU的时间片，此时有数据，它进行消费，然后进入wait状态
     * -    如果此时任意一个生产者获取了时间片都可以继续执行下去
     * 但是，如果此时C2得到了CPU的时间片，它发现此时已经没有了数据，便进入了wait的状态
     * <p>
     * 上面的情况并不是那么清晰，也就是说这些线程肯定会执行操作一些数据后才会交替进入wait状态，不会在一开始就都进入假死状态
     * 本质上还是CPU时间片的轮询随机性，以及notify方法的随机性
     * <p>
     * 注意：虽然每个线程都会执行notify方法，但不一定能唤醒我们设想的线程，可能导致所有的线程进入wait的状态，最终产生了假死的现象
     */
    @Test
    public void testMultiPC() {
        ProducerConsumerVersion2 pc = new ProducerConsumerVersion2();
        Stream.of("P1", "P2").forEach(p -> {
            new Thread(p) {
                @Override
                public void run() {
                    while (true) {
                        pc.produce();
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2").forEach(c -> {
            new Thread(c) {
                @Override
                public void run() {
                    while (true) {
                        pc.consume();
                    }
                }
            }.start();
        });
    }
}
