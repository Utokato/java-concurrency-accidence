package cn.utokato.juc.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/25
 */
public class ThreadPoolExecutorExample2 {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20),
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true); // 将线程池中的线程设置为守护线程，当主线程结束时，这些守护线程也会随之结束
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy()
        );

        // 处理并行(parallel)任务
        IntStream.range(0, 10).forEach(i -> {
            executor.submit(() -> {
                while (true) {
                    // 模拟耗时任务
                }
            });
        });

        // 处理串行(sequence)任务
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("========over========");

    }
}
