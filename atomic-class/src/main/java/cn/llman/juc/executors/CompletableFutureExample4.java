package cn.llman.juc.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * {@link CompletableFuture} 组合方法
 *
 * @author lma
 * @date 2019/06/05
 */
public class CompletableFutureExample4 {
    public static void main(String[] args) throws InterruptedException {
        // thenAcceptBoth();
        acceptEither();

        Thread.currentThread().join();


    }

    private static void thenCompose(){

    }

    private static void thenCombine(){

    }

    private static void runAfterBoth() {

    }

    private static void runAfterEither() {

    }

    private static void acceptEither() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Start acceptEither-1.");
            nap(3);
            System.out.println("End acceptEither-1.");
            return "acceptEither-1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("Start acceptEither-2.");
            nap(3);
            System.out.println("End acceptEither-2.");
            return "acceptEither-2";
        }), System.out::println);
    }

    private static void thenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Start supplyAsync working.");
            nap(3);
            System.out.println("End supplyAsync working.");
            return "thenAcceptBoth";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println("Start thenAcceptBoth working.");
            nap(3);
            System.out.println("End thenAcceptBoth working.");
            return 100;
        }), (s, i) -> System.out.println(s + i));
    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
