package cn.utoakto.concurrency.chapter9;

/**
 * 生产消费者模式 版本一
 * 线程间没有相互通信，生产线程拿到时间片后疯狂生产数据，消费线程拿到时间片后只能消费当前的一个数据，历史数据都无法消费
 *
 * @date 2019/4/9
 */
public class ProducerConsumerVersion1 {

    private int i = 1;
    private final Object LOCK = new Object();

    private void produce() {
        synchronized (LOCK) {
            System.out.println("Produce data --> " + (i++));
        }
    }

    private void consume() {
        synchronized (LOCK) {
            System.out.println("Consume data --> " + i);
        }
    }

    public static void main(String[] args) {
        ProducerConsumerVersion1 pc = new ProducerConsumerVersion1();

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

}
