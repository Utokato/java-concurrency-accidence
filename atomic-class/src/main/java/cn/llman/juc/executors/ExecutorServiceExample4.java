package cn.llman.juc.executors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date 2019/5/28
 */
public class ExecutorServiceExample4 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws Exception {
        // testInvokeAny();
        // testInvokeAnyTimeout();
        // testInvokeAll();
        // testInvokeAllTimeout();
        // testSubmitRunnable();
        testSubmitRunnableWithResult();


    }

    /**
     * {@link ExecutorService#invokeAny(Collection)} 执行callableList其中的任意一个callable
     * 一旦执行了该callable后会返回一个结果，其他的callable就不会被执行了
     * <p>
     * 注意：invokeAny 是一个阻塞方法(同步方法)，只有当callable中的逻辑执行完毕后，才会继续向下执行
     */
    private static void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            nap(5);
            return i;
        }).collect(Collectors.toList());

        Integer result = threadPool.invokeAny(callableList);
        System.out.println("invokeAny result is " + result);

        System.out.println("==============");
    }

    private static void testInvokeAnyTimeout() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        Integer result = null;
        try {
            result = threadPool.invokeAny(
                    IntStream.range(0, 5)
                            .boxed()
                            .map(i -> (Callable<Integer>) () -> {
                                nap(5);
                                return i;
                            }).collect(Collectors.toList()),
                    3L,
                    TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("invokeAny result is " + result);

        System.out.println("==============");
    }

    /**
     * {@link ExecutorService#invokeAll(Collection)}
     * <p>
     * 注意：invokeAll 是一个阻塞方法(同步方法)，只有当所有callable中的逻辑执行完毕后，才会继续向下执行
     */
    private static void testInvokeAll() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<Integer>> result = new ArrayList<>();
        try {
            result = threadPool.invokeAll(
                    IntStream.range(0, 5)
                            .boxed()
                            .map(i -> (Callable<Integer>) () -> {
                                nap(ThreadLocalRandom.current().nextInt(20));
                                return i;
                            }).collect(Collectors.toList())
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=============");

        result.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);

    }

    /**
     * {@link ExecutorService#invokeAll(Collection, long, TimeUnit)}
     * 会抛出超时异常
     * <p>
     * 这里需要注意到一个问题就是，执行需要分阶段的
     * 如：线程池中的线程都先去处理任务，统一的返回一个future(票据)
     * -   所有的任务执行完毕后，在轮询每一个future去获取结果
     * -   获取到所有的结果以后，再进行下一个阶段的任务
     * <p>
     * 能不能，其中的某一个任务处理完毕，就立马去获取结果，再去执行之后的逻辑?
     * RxJava Reactor
     * 响应式编程 <b>Reactive Programming</b>
     */
    private static void testInvokeAllTimeout() throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.invokeAll(
                IntStream.range(0, 5)
                        .boxed()
                        .map(i -> (Callable<Integer>) () -> {
                            System.out.println(Thread.currentThread().getName() + " running.");
                            nap(ThreadLocalRandom.current().nextInt(10));
                            return i;
                        }).collect(Collectors.toList()),
                20L,
                TimeUnit.SECONDS
        ).stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);

        System.out.println("=======finished======");

    }

    /**
     * {@link ExecutorService#submit(Runnable)}
     */
    private static void testSubmitRunnable() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Future<?> future = threadPool.submit(() -> {
            nap(3);
        });
        Object oNull = future.get(); // blocked method
        System.out.println("Result: " + oNull);
    }

    /**
     * {@link ExecutorService#submit(Runnable, Object)}
     */
    private static void testSubmitRunnableWithResult() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        String flag = "Done";
        Future<String> future = threadPool.submit(() -> {
            nap(3);
        }, flag);
        String result = future.get();
        System.out.println("Result: " + result);
    }


    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }
    }
}
