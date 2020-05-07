package cn.utoakto.concurrency.chapter17;

/**
 * Worker 模式
 * 本质上 与 生产-消费模式类似
 * 生产者与消费者 之间的桥梁就是任务队列
 * 生产者：向任务队列中添加任务，队列满了以后，生产者进入wait状态
 * 消费者：从任务队列中获取任务，队列为空以后，消费者进入wait状态
 * 无论是生产者还是消费者，它们在操作队列以后，都会执行notifyAll的方法，唤醒所有的线程工作
 * 这些被唤醒的线程，也就是生产者或消费者，都会去判断队列的状态
 * 如果任务队列为空，消费者继续wait
 * 如果任务队列满了，生产者继续wait
 *
 * 队列 是一种基本的数据结构，可以使用数组或链表来实现。
 *
 * @date 2019/5/7
 */
public class Client {
    public static void main(String[] args) {
        final Channel channel = new Channel(5);

        channel.startWorker();

        new TransportThread("Alisa", channel).start();
        new TransportThread("Bob", channel).start();
        new TransportThread("Caterina", channel).start();

    }
}
