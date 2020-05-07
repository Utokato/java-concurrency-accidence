package cn.utokato.juc.executors;

import java.util.concurrent.*;

/**
 * @date 2019/6/1
 */
public class CompletableFutureExample2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // testSupplyAsync();
        /*Future<Void> future = testRunAsync();
        future.get();*/

        /*Future<Void> voidFuture = testCompletedFuture("Hello");
        voidFuture.get();*/

        // System.out.println("--->"+testAnyOf().get());

        testAllOf();


        Thread.currentThread().join();
    }

    private static void create(){
        CompletableFuture<Object> future = new CompletableFuture<>();

        // 不建议使用构造函数new CompletableFuture对象，尽量使用工厂方法
    }

    /**
     * 执行多个CompletableFuture，不关心返回结果
     */
    private static void testAllOf(){
        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
                    try {
                        System.out.println("1----start");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("1----end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).whenComplete((v, t) -> System.out.println("1----Finished")),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("2----start");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("2----end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Hello";
                }).whenComplete((v, t) -> System.out.println("2----Finished")));
    }

    /**
     * 执行多个CompletableFuture，返回其中任意一个值
     * @return
     */
    private static Future<?> testAnyOf() {
        return CompletableFuture.anyOf(CompletableFuture.runAsync(() -> {
                    try {
                        System.out.println("1----start");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("1----end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).whenComplete((v, t) -> System.out.println("1----Finished")),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("2----start");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("2----end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Hello";
                }).whenComplete((v, t) -> System.out.println("2----Finished")));
    }

    private static Future<Void> testCompletedFuture(String data) {
        return CompletableFuture.completedFuture(data).thenAccept(System.out::println);
    }

    private static Future<Void> testRunAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Obj->start");
                TimeUnit.SECONDS.sleep(6);
                System.out.println("Obj->end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((v, t) -> {
            System.out.println("====over====");
        });
    }

    /**
     * {@link CompletableFuture#runAfterBoth(CompletionStage, Runnable)}
     * 一个CompletableFuture可以与另外一个CompletableFuture同时异步地执行
     * 当这两个都执行完了以后，会继续执行runnable中逻辑
     * <p>
     * 举例
     * 当一个用户post提交了个人信息，basic信息和detail信息
     * 需要分别inset这两部分的内容，都结束已经再执行其他的逻辑
     * <p>
     * post msg -> insert basic msg     ->  other handler
     * -        -> insert detail msg    /
     */
    private static void testSupplyAsync() {
        CompletableFuture
                .supplyAsync(Object::new)
                .thenAcceptAsync(o -> {
                    try {
                        System.out.println("Obj->start");
                        TimeUnit.SECONDS.sleep(5);
                        System.out.println("Obj->end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                .runAfterBoth(CompletableFuture.supplyAsync(() -> "Hello")
                                .thenAcceptAsync(
                                        s -> {
                                            try {
                                                System.out.println("Hello->start");
                                                TimeUnit.SECONDS.sleep(3);
                                                System.out.println("Hello->end");
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                ),
                        () -> System.out.println("All is finished."));
    }


}
