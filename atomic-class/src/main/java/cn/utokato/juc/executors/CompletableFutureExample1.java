package cn.utokato.juc.executors;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date 2019/5/30
 */
public class CompletableFutureExample1 {
    public static void main(String[] args) throws Exception {
        // doWithFuture();
        // doWithCompletableFuture();
        // doWithFuture1();
        doWithCompletableFuture1();

    }

    /**
     * {@link CompletableFuture} 实现了异步回调的机制
     *
     * 当一个线程执行完本阶段的任务后，就可以继续执行下一个阶段的任务
     * 而不是等待所有线程都执行完本阶段的任务，实现了真正意义的并行化
     */
    private static void doWithCompletableFuture1() throws InterruptedException {
        IntStream.range(0, 5)
                .boxed()
                .forEach(integer -> CompletableFuture.supplyAsync(CompletableFutureExample1::get)
                        .thenAccept(CompletableFutureExample1::display)
                        .whenComplete((aVoid, throwable) -> {
                            System.out.println(integer + " done!");
                        }));

        Thread.currentThread().join();
    }

    private static void doWithFuture1() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> tasks = IntStream.range(0, 5)
                .boxed()
                .map(i -> (Callable<Integer>) CompletableFutureExample1::get)
                .collect(Collectors.toList());
        executorService.invokeAll(tasks).stream().map(integerFuture -> {
            try {
                // 阻塞方法，只有阶段性的任务执行完毕后，才能继续执行。如果遇到一个非常耗时的任务，所有的线程都需要等待
                return integerFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).parallel().forEach(CompletableFutureExample1::display);
    }

    private static void display(int value) {
        System.out.println(Thread.currentThread().getName() + " display will sleep " + value + " s.");
        nap(value);
        System.out.println(Thread.currentThread().getName() + " display executes done!");
    }

    private static int get() {
        int nextInt = ThreadLocalRandom.current().nextInt(10);
        System.out.println(Thread.currentThread().getName() + " get will sleep " + nextInt + " s.");
        nap(nextInt);
        System.out.println(Thread.currentThread().getName() + " get executes done!");
        return nextInt;
    }

    private static void doWithCompletableFuture() throws InterruptedException {
        CompletableFuture.runAsync(() -> nap(5)).whenComplete((aVoid, throwable) -> {
            System.out.println("Done!");
            System.out.println(aVoid);
            throwable.printStackTrace();
        });

        System.out.println("=====Finished=====");

        Thread.currentThread().join();
    }


    /**
     * 缺陷在于，我们不知道何时异步任务执行完毕。
     * 需要一种异步回调的机制来解决这个问题
     */
    private static void doWithFuture() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 利用线程池来实现异步调用
        Future<?> future = executorService.submit(() -> nap(5));

        while (!future.isDone()) {
            // do nothing
        }

        System.out.println("Done!");

    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
