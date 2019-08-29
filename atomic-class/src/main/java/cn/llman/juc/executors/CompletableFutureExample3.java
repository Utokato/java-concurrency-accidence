package cn.llman.juc.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 测试{@link CompletableFuture}的中间操作
 *
 * @author lma
 * @date 2019/06/01
 */
public class CompletableFutureExample3 {

    static CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

    public static void main(String[] args) throws ExecutionException, InterruptedException {

    }

    /* ------- 中间操作 - 有返回值 -------- */
    private static void testWhenComplete() {
        CompletableFuture<String> completable = future.whenComplete((v, t) -> System.out.println(v));
        CompletableFuture<String> completeAsync = future.whenCompleteAsync((v, t) -> System.out.println(v));
    }

    private static void testThenApply() {
        CompletableFuture<Integer> thenAccept = future.thenApply(String::length);
        CompletableFuture<Void> thenAcceptAsync = future.thenAcceptAsync(String::length);
    }

    private static void testHandle() {
        CompletableFuture<Integer> handle = future.handle((v, t) -> v.length());
        CompletableFuture<Integer> handleAsync = future.handleAsync((v, t) -> v.length());
    }

    private static void testToCompletableFuture() {
        CompletableFuture<String> future = CompletableFutureExample3.future.toCompletableFuture();
    }

    /* ------- 中间操作 - 无返回值 -------- */
    private static void testThenAccept() {
        CompletableFuture<Void> thenAccept = future.thenAccept(System.out::println);
        CompletableFuture<Void> thenAcceptAsync = future.thenAcceptAsync(System.out::println);
    }

    private static void testThenRun() {
        CompletableFuture<Void> thenRun = future.thenRun(() -> System.out.println("Done!"));
        CompletableFuture<Void> thenRunAsync = future.thenRunAsync(() -> System.out.println("Done!"));
    }
}
