package cn.llman.juc.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/28
 */
public class ExecutorServiceExample2 {

    /**
     * {@link RejectedExecutionHandler}
     */
    public static void main(String[] args) {
        // testAbortPolicy();
        // testDiscardPolicy();
        // testCallerRunsPolicy();
        testDiscardOldestPolicy();


    }

    /**
     * {@link ThreadPoolExecutor.AbortPolicy} 丢弃并抛出异常
     */
    private static void testAbortPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.AbortPolicy()
        );
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> nap(60));
        }
        nap(1);
        executor.execute(() -> System.out.println("**********"));
    }

    /**
     * {@link ThreadPoolExecutor.DiscardPolicy} 抛弃并且不会抛出异常
     */
    private static void testDiscardPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.DiscardPolicy()
        );
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> nap(60));
        }
        nap(1);
        executor.execute(() -> System.out.println("**********"));

        System.out.println("=================");
    }

    /**
     * {@link ThreadPoolExecutor.CallerRunsPolicy}
     * 过载的任务，会通过当前启动线程池的线程去执行
     * 不会使用线程池内部的线程
     * 如，该实例中过载的task由main方法去执行
     */
    private static void testCallerRunsPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> nap(60));
        }
        nap(1);
        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " go to exec more task.");
        });

    }

    /**
     * {@link ThreadPoolExecutor.DiscardOldestPolicy} 抛弃任务队列中最旧的任务，为新的任务腾出位置
     */
    private static void testDiscardOldestPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                System.out.println("From lambda task.");
                nap(10);
            });
        }
        nap(1);
        executor.execute(() -> {
            System.out.println("Will be running with discard oldest task.");
        });

    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
