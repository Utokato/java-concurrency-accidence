package cn.llman.concurrency.chapter13;

import java.util.stream.IntStream;

/**
 * @date 2019/5/3
 */
public class ProducerAndConsumerClient {
    public static void main(String[] args) {
        final MessageQueue messageQueue = new MessageQueue();

        IntStream.rangeClosed(1, 5).forEach(i -> new ProducerThread(messageQueue, i).start());
        IntStream.rangeClosed(1, 2).forEach(i -> new ConsumerThread(messageQueue, i).start());

    }
}
