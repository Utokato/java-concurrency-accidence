package cn.utoakto.concurrency.chapter15;

import java.util.stream.IntStream;

/**
 * @date 2019/4/11
 */
public class ThreadPoolVersion1Test {

    public static void main(String[] args) {
        ThreadPoolVersion1 threadPool = new ThreadPoolVersion1();
        IntStream.rangeClosed(0, 50).forEach(i -> threadPool.submit(() -> {
            System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " start.");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " finished!");
        }));
    }

}
