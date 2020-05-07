package cn.utokato.juc.executors;

import java.util.concurrent.*;

/**
 * @date 2019/5/28
 */
public class FutureExample1 {
    public static void main(String[] args) throws Exception {
        // testFutureGet();
        testFutureGetTimeout();

    }

    /**
     * {@link Future#get()}
     */
    private static void testFutureGet() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(() -> {
            nap(10);
            return 10;
        });

        System.out.println("I will be executed immediately");
        Thread callerThread = Thread.currentThread();

        new Thread(() -> {
            nap(1);
            callerThread.interrupt();
        }).start();

        // ----------
        Integer result = future.get();

        System.out.println(result);

    }

    /**
     * {@link Future#get(long, TimeUnit)}
     * 设置超时时间
     * 超过了这个设置的时间以后，get方法就不会继续blocked
     * 但是，正在执行的线程并不会被打断，也就是说任务还会继续执行
     * 如果需要在超时后终止任务的执行，需要添加额外的终止操作
     */
    private static void testFutureGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(() -> {
            nap(10);
            System.out.println("=======exec=======");
            return 10;
        });
        System.out.println("I will be executed immediately");

        // ----------
        Integer result = future.get(5, TimeUnit.SECONDS);

        System.out.println(result);
    }


    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
