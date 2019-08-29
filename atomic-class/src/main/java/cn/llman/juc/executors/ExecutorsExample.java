package cn.llman.juc.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/25
 */
public class ExecutorsExample {

    public static void main(String[] args) {

        // useCachedThreadPool();
        // useFixedThreadPool();
        useSingleThreadExecutor();

    }

    /**
     * 只有一个线程的线程池 VS 单个线程
     * 单个线程在执行完任务单元后会shutdown，而SingleThreadExecutor不会死亡
     * 单个线程不能将多余的任务单元存放到一个任务队列中
     */
    private static void useSingleThreadExecutor() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        IntStream.range(0, 100).forEach(i -> service.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }));
    }

    /**
     * 固定线程数量的线程池
     */
    private static void useFixedThreadPool() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        System.out.println("ActiveCount" + executor.getActiveCount());
        IntStream.range(0, 100).forEach(i -> executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }));
        System.out.println("ActiveCount" + executor.getActiveCount());
    }

    /**
     * These pools will typically improve the performance
     * of programs that execute many short-lived asynchronous tasks.
     */
    private static void useCachedThreadPool() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        System.out.println("ActiveCount" + executor.getActiveCount());
        executor.execute(() -> {
            System.out.println("=====");
        });
        System.out.println("ActiveCount" + executor.getActiveCount());
        IntStream.range(0, 100).forEach(i -> executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ActiveCount" + executor.getActiveCount());
    }
}
