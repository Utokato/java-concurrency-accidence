package cn.utoakto.concurrency.chapter9;

import java.util.stream.Stream;

/**
 * 生产消费者模式 版本三
 *
 * @date 2019/4/9
 */
public class ProducerConsumerVersion3 {

    private int i = 0;
    private final Object LOCK = new Object();
    private volatile boolean isProduced = false;


    private void produce() {
        synchronized (LOCK) {
            /**
             * 如果这里使用的if判断
             * 当多个消费线程交替没有满足if判断条件，当其中一个线程生产了数据后
             * 按照设想，其他的消费线程便不能再次进行数据的生产
             * 但是其他线程获取了时间片后，它们已经通过了if判断条件，所以会接着执行，导致数据的重复生产，这样会导致，上一个生产的数据被覆盖，即出现了数据丢失的情况
             * <p>
             * 如果是while判断的话，即使是其他线程得到了时间片，还要再次经过while判断，能保证数据的唯一生产，不会出现数据丢失的情况
             */
            while (isProduced) { // 注意此处 while 的用法，区别与 if
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            System.out.println("Produce data --> " + i + " in a thread named: " + Thread.currentThread().getName());
            // to do notifyAll
            LOCK.notifyAll();
            isProduced = true;
        }
    }

    private void consume() {
        synchronized (LOCK) {

            /**
             * 如果这里使用的if判断
             * 当多个消费线程交替没有满足if判断条件，当其中一个线程消费了数据后
             * 按照设想，其他的消费线程便不能再次进行数据的消费
             * 但是其他线程获取了时间片后，它们已经通过了if判断条件，所以会接着执行，导致数据的重复消费
             * <p>
             * 如果是while判断的话，即使是其他线程得到了时间片，还要再次经过while判断，能保证数据的唯一消费
             */
            while (!isProduced) { // 注意此处 while 的用法，区别与 if
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Consume data --> " + i + " in a thread named: " + Thread.currentThread().getName());
            // to do notifyAll
            LOCK.notifyAll();
            isProduced = false;
        }
    }

    public static void main(String[] args) {
        ProducerConsumerVersion3 pc = new ProducerConsumerVersion3();
        Stream.of("P1", "P2", "P3", "P4").forEach(p -> {
            new Thread(p) {
                @Override
                public void run() {
                    while (true) {
                        pc.produce();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2", "C3", "C4").forEach(c -> {
            new Thread(c) {
                @Override
                public void run() {
                    while (true) {
                        pc.consume();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });
    }

}
