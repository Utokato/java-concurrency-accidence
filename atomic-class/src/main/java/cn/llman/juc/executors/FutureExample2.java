package cn.llman.juc.executors;

import java.util.concurrent.*;

/**
 * @date 2019/5/28
 */
public class FutureExample2 {
    public static void main(String[] args) {
        // testIsDone();
        // testCancel();
        testCancel2();
    }

    /**
     * Completion may be due to normal termination, an exception, or cancellation
     * -- in all of these cases, this method will return true
     * <p>
     * isDone: 正常结束、出现异常、取消时，返回true
     */
    private static void testIsDone() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(() -> {
            nap(10);
            System.out.println("=======exec=======");
            return 10;
        });

        System.out.println("I will be executed immediately");

        System.out.println(future.isDone());
    }

    /**
     * {@link Future#cancel(boolean)}
     * cancel 失败的情形：
     * 1. 任务已经执行完成了
     * 2. 任务已经被取消了
     * <p>
     * 当任务被成功取消了，{@link Future#isCancelled()} 和 {@link Future#isDone()} 都会返回true
     */
    private static void testCancel() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(() -> {
            nap(10);
            System.out.println("=======exec=======");
            return 10;
        });
        nap(1);
        System.out.println("I will be executed immediately");

        System.out.println("cancel: " + future.cancel(true));
        System.out.println("isDone: " + future.isDone());
        System.out.println("isCancelled: " + future.isCancelled());


    }

    /**
     * {@link Future#cancel(boolean)}
     * cancel后，线程内部任何还会继续执行，只是我们不能再通过{@link Future#get()}获取结果，或抛出CancellationException
     * <p>
     * 怎样能在cancel后，同时终止线程内部的任务呢？
     * 1. 配合判断线程的状态，Thread.interrupted()，cancel是会打断线程。如果线程被打断了，就终止执行
     * 2. 在构造线程池时，设置线程为守护线程，这样会随着主线程的结束而结束，当cancel后，整个程序都会终止
     */
    private static void testCancel2() {
        ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true); // 设置线程池中的线程为守护线程
                return thread;
            }
        });
        Future<Integer> future = executorService.submit(() -> {
            while (!Thread.interrupted()) {
                // 模拟耗时任务，如果线程被打断了，就不会继续执行任务
            }
            return 10;
        });
        nap(1);
        System.out.println("I will be executed immediately");

        System.out.println("cancel: " + future.cancel(true)); //
        System.out.println("isDone: " + future.isDone());
        System.out.println("isCancelled: " + future.isCancelled());


    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }
    }
}
