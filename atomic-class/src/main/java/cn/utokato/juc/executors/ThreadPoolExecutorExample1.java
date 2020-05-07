package cn.utokato.juc.executors;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/25
 */
public class ThreadPoolExecutorExample1 {

    /**
     * 注意线程池中，核心线程数量、最大线程数量、当前活跃线程数量、工作队列大小等这些数量之间的关系
     *
     * 总体来说，当任务少时，线程池负载小，当前活跃线程数量维持在核心线程数量大小；
     * -       当任务多时，线程池负载增加，会动态地扩充当前活跃线程的数量，但最大不会超过最大线程的数量；
     * -       当大部分任务数理完毕后，线程池的负载减小，会动态的缩减当前活跃线程的数量。
     * -       不是立即缩减，而是经过了keepAliveTime时间之后才开始缩减，缩减到核心线程数量大小
     *
     * {@link ThreadPoolExecutor#shutdown()} VS {@link ThreadPoolExecutor#shutdownNow()}
     * shutdown()：线程池会保证将当前所有的任务处理完毕再关闭，前提是这些任务中没有cache InterruptedException
     * shutdownNow()：线程池会将当前核心线程执行的任务处理完毕，并将任务队列中任务返回，之后再关闭线程池
     *
     */
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20),
                Thread::new,
                new ThreadPoolExecutor.AbortPolicy()
        );

        IntStream.range(0, 35).forEach(i -> executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + " " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        // executor.shutdown(); // pool终止，会将所有的任务执行结束后再终止
        List<Runnable> runnableList = executor.shutdownNow();// pool终止，会将所有core线程正在执行的任务执行结束，并将BlockingQueue中的任务返回
        System.out.println(runnableList.size());
        try {
            executor.awaitTermination(1,TimeUnit.HOURS); // 等待终止
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("=======over=======");
        // other works
    }
}
