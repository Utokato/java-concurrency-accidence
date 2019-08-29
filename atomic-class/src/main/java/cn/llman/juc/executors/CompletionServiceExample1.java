package cn.llman.juc.executors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @date 2019/5/28
 */
public class CompletionServiceExample1 {
    public static void main(String[] args) throws Exception {
        // testFutureDefect1();
        testFutureDefect2();

    }

    /**
     * {@link Future} 没有callback机制，调用get()方法是会陷入阻塞
     */
    private static void testFutureDefect1() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        Future<Integer> future = service.submit(() -> {
            nap(5);
            return 10;
        });

        System.out.println("I will be invoked immediately");
        Integer result = future.get();// Fall into blocked and there is no callback method
        System.out.println(result);
    }

    /**
     * 当线程中执行多个任务时，每个任务消耗的时间不一样
     * {@link Future}不能实现，先执行完的任务先获取结果
     * <p>
     * 如果我们恰好需要获取一个耗时较长的任务的返回结果时，所有的任务返回的结果都会陷入了阻塞
     * 不能灵活地每执行完一个结果就立即获取
     */
    private static void testFutureDefect2() throws InterruptedException {
        List<Callable<Integer>> callableList = Arrays.asList(() -> {
            nap(10);
            System.out.println("The 10 finished!");
            return 10;
        }, () -> {
            nap(20);
            System.out.println("The 20 finished!");
            return 20;
        });
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.invokeAll(callableList).forEach(integerFuture -> {
            try {
                System.out.println(integerFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }


    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
