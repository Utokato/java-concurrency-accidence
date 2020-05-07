package cn.utokato.juc.executors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date 2019/5/25
 */
public class ExecutorsExample2 {

    /**
     * {@link Executors#newWorkStealingPool()}
     * 会根据当前的CPU核心数 开辟线程
     * 返回一个future (future的设计模式)
     * 后面可以通过future来获取执行的结果
     */
    public static void main(String[] args) throws InterruptedException {
        // Optional.of(Runtime.getRuntime().availableProcessors()).ifPresent(System.out::println);

        ExecutorService service = Executors.newWorkStealingPool();

        List<Callable<String>> callableList = IntStream
                .range(0, 20)
                .boxed()
                .map(i -> (Callable<String>) () -> {
                    Optional.of("Thread: " + Thread.currentThread().getName()).ifPresent(System.out::println);
                    nap(2);
                    return "Task - " + i;
                })
                .collect(Collectors.toList());

        List<Future<String>> futures = service.invokeAll(callableList);

        futures.stream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);


    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
