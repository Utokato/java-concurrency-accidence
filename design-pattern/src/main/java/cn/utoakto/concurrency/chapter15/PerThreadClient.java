package cn.utoakto.concurrency.chapter15;

import java.util.stream.IntStream;

/**
 * Thread-Per-Message Design Pattern
 *
 * @date 2019/5/5
 */
public class PerThreadClient {

    public static void main(String[] args) {
        MessageHandler handler = new MessageHandler();
        // IntStream.rangeClosed(0, 10).forEach(i -> handler.request(new Message(String.valueOf(i))));
        IntStream.rangeClosed(0, 10).forEach(i -> handler.requestByThreadPool(new Message(String.valueOf(i))));
        handler.shotdown();
    }
}
