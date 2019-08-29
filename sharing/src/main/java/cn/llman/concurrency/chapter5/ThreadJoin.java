package cn.llman.concurrency.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @date 2019/4/8
 */
public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 100)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->index：" + i));
        });
        Thread t2 = new Thread(() -> {
            IntStream.range(1, 100)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->index：" + i));
        });

        t1.start();
        t2.start();
        t1.join(); // join 需要放在 start后 // join()不加参数，表示必须等到t1线程执行完毕后，main线程才会开始继续执行
        t2.join();


        // 对于main线程而言，由于t1和t2都执行了join方法，所以main线程会等到t1和t2都执行完后才会执行
        // 当对于t1和t2而言，它们之间是竞争并发多线程来执行执行的
        Optional.of("All of tasks finish done!").ifPresent(System.out::println);

        IntStream.range(1, 100)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->index：" + i));

    }
}
