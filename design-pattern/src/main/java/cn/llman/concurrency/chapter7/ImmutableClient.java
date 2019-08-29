package cn.llman.concurrency.chapter7;

import java.util.stream.IntStream;

/**
 * {@link String} 是一个典型的不可变对象
 * <p>
 * 不可变对象，一定是一个线程安全的对象，如{@link String}
 * 反过来就不一定了
 * 可变对象，一定是线程不安全的对象吗? 答案是否定的，即不一定是线程不安全的对象，如{@link StringBuffer}
 *
 * @date 2019/5/2
 */
public class ImmutableClient {


    public static void main(String[] args) {
        // share data that is immutable
        Person person = new Person("Alisa", "PingLu");

        IntStream.rangeClosed(0, 5).forEach(i -> new UsePersonThread(person).start());

    }
}
