package cn.llman.concurrency.chapter11;

import java.util.stream.IntStream;

/**
 * 和Spring中的 applicationContext相比较
 *
 * @date 2019/5/3
 */
public class ContextTest {
    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i -> new Thread(new ExecutionTask()).start());
    }
}
