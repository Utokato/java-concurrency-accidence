package cn.utokato.juc.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * {@link CompletableFuture} 终止方法
 *
 * @author lma
 * @date 2019/06/05
 */
public class CompletableFutureExample5 {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // getNow();
        // complete();
        // join();
        // completeExceptionally();
        obtrudeException();

        // errorHandle().whenCompleteAsync((v, t) -> System.out.println(v + ", in early stage"));

        Thread.currentThread().join();
    }

    private static CompletableFuture<String> errorHandle() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(3);
            System.out.println("=== working in early stage.");
            return "Hello";
        });

        future.thenApply(s -> {
            Integer.parseInt(s); // 出现异常
            nap(5);
            System.out.println("=== working in later stage.");
            return s + "World";
        }).exceptionally(Throwable::getMessage) // 对异常进行处理
                .thenAccept(System.out::println);

        return future;
    }

    /**
     * {@link CompletableFuture#obtrudeException(Throwable)}
     * 调用了obtrudeException方法后，直接使用Throwable来替换future的结果
     * 没有返回值
     *
     */
    private static void obtrudeException() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(3);
            System.out.println("=== obtrude Exception working.");
            return "Hello";
        });
        future.obtrudeException(new Exception("I meet an error"));
        System.out.println(future.get());
    }

    /**
     * {@link CompletableFuture#completeExceptionally(Throwable)}
     * 调用了completeExceptionally方法后，如果CompletableFuture内部的逻辑已经正常返回一个结果了
     * completeExceptionally不会对该结果进行替换
     * 如果CompletableFuture内部的逻辑执行比较耗时，没有返回一个结果
     * completeExceptionally会以传入的Throwable异常替换原来的结果
     * 最终future.get()的结果可能会受到影响
     * <p>
     * completeExceptionally返回一个Boolean值
     * true表示complete对结果进行了替换;false表示complete没有对结果进行替换
     */
    private static void completeExceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(3);
            System.out.println("=== complete Exceptionally working.");
            return "Hello";
        });
        nap(4);
        boolean isCompleted = future.completeExceptionally(new RuntimeException());
        System.out.println(isCompleted);
        System.out.println(future.get());
    }

    /**
     * {@link CompletableFuture#join()} & {@link CompletableFuture#get()}
     * <p>
     * 为了更加轻便的使用函数式的调用，join中对异常进行了处理。使得整个的调用更加的简洁
     */
    private static void join() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(3);
            System.out.println("=== Join working.");
            return "Hello";
        });

        String result = future.join();
        System.out.println(result);
    }


    /**
     * {@link CompletableFuture#complete(Object)}
     * 调用了complete方法后，如果CompletableFuture内部的逻辑已经正常返回一个结果了
     * complete不会对该结果进行替换
     * 如果CompletableFuture内部的逻辑执行比较耗时，没有返回一个结果
     * complete会以传入的Object替换原来的结果
     * 最终future.get()的结果可能会受到影响
     * <p>
     * complete返回一个Boolean值
     * true表示complete对结果进行了替换;false表示complete没有对结果进行替换
     */
    private static void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(3);
            System.out.println("=== Complete working.");
            return "Hello";

        });
        nap(1);
        boolean isComplete = future.complete("World");
        System.out.println(isComplete);
        System.out.println(future.get());
    }

    /**
     * {@link CompletableFuture#getNow(Object)}
     * 调用了getNow方法后，会立即返回一个结果Object
     * 并不会影响future.get()的结果
     */
    private static void getNow() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            nap(1);
            return "Hello";
        });
        // nap(2);
        String result = future.getNow("World");
        System.out.println("getNow: " + result);
        System.out.println("get: " + future.get());
    }


    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
