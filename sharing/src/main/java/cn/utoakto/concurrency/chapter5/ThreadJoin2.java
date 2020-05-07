package cn.utoakto.concurrency.chapter5;

import org.junit.Test;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @date 2019/4/8
 */
public class ThreadJoin2 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("t1 is running...");
                Thread.sleep(10_000);
                System.out.println("t1 is done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t1.join(100, 100); // main线程会等待t1线程100ms又100ns，过了等待时间，不论t1是否执行完成，main线程都会执行

        Optional.of("All of tasks finish done!").ifPresent(System.out::println);

        IntStream.range(1, 100)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->index：" + i));

    }

    @Test
    public void testThreadJoin() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        // 这是一个很神奇的语法
        // 当前线程执行了join方法后，程序就不会终止，一直运行下去
        // 这是因为，这个线程在等待它自己执行完毕，这是一个递归的等待
        // 这个线程在等它自己执行完毕，它自己在执行什么? 它在执行一个等待自己执行完毕的程序 ...
        Thread.currentThread().join();


        // 常用的场景，一些http服务器的服务是一个守护线程，会随着main线程的死亡而死亡
        // 为了不让这些服务线程死掉，需要一个解决方法：Thread.currentThread().join();
        // start HTTPServer
        // JettyHttpServer.start();
    }

}
