package cn.utokato.juc.executors;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/25
 */
public class ThreadPoolExecutorCreate {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = createThreadPoolExecutor();

        int activeCount = -1;
        int queueSize = -1;


        while (true) {
            if (executor.getActiveCount() != activeCount || executor.getQueue().size() != queueSize) {
                System.out.println("CorePoolSize: " + executor.getCorePoolSize());
                System.out.println("MaximumPoolSize: " + executor.getMaximumPoolSize());
                System.out.println("ActiveCount: " + executor.getActiveCount());
                System.out.println("QueueSize: " + executor.getQueue().size());
                activeCount = executor.getActiveCount();
                queueSize = executor.getQueue().size();
                System.out.println("===============================");
            }
        }
    }

    /**
     * int corePoolSize, // 线程池中核心线程的数量
     * int maximumPoolSize, // 线程池中最多线程的数量
     * long keepAliveTime, // 线程池中，大于核心线程数量的多余线程存活的时间
     * TimeUnit unit, // 时间单位
     * BlockingQueue<Runnable> workQueue, // 任务队列，任务数量大于最大线程数量时，会被放到任务队列中
     * ThreadFactory threadFactory, // 线程工厂
     * RejectedExecutionHandler handler // 当任务队列中满了以后，线程池的拒绝策略
     * <p>
     * 梳理corePoolSize、maximumPoolSize、queueSize、keepAliveTime之间的关系：
     * workQueue：the queue to use for holding tasks before they are executed
     * 在源码中有这样一句话，所有的任务在执行前都被工作队列控制，这说明所有的任务(Runnable)都会进入工作队列中
     * 工作队列在初始时会设置其容量(capacity)
     * <p>
     * 标记：
     * <param>corePoolSize -> x</param>
     * <param>maximumPoolSize -> y</param>
     * <param>queueSize -> z</param>
     * <param>keepAliveTime -> s</param>
     * <p>
     * 对于某个线程池而言，一次性最大提交的任务数量(t)为 (y+z)
     * 当 t > (y+z) 时，多余的任务会被回收策略处理
     * 当 t <= (y+z) 时，线程池会开辟线程进行处理，开辟线程数量的原则为：
     * -     当 t <= x 时，开辟 t 个线程进行任务处理
     * -     当 x <= t <= y 时，会将 (t-x) 任务放置到任务队列中，此时开辟x个线程进行处理
     * -     当 y < t <= (y+z) 时，会开辟 (t-z) 个线程进行处理
     * 这里需要说明的一点是，t < z 时，只会开辟 x 个线程进行处理
     */
    private static ThreadPoolExecutor createThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.AbortPolicy()
        );
        System.out.println("== ThreadPoolExecutor created done ==");


        executor.execute(() -> nap(10));
        executor.execute(() -> nap(10));
        executor.execute(() -> nap(10));
        // executor.execute(()-> nap(10));

        return executor;
    }

    private static void nap(int sec) {
        try {
            System.out.println("* " + Thread.currentThread().getName() + " *");
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param corePoolSize      线程池中核心线程数量
     * @param maximumPoolSize   线程池中最大激活线程数量
     * @param blockingQueueSize 阻塞任务队列大小
     * @param toSubmitTaskNum   待提交的任务数量
     * @return 线程池中真正激活的线程数量
     */
    private static int howManyThreadsToActive(int corePoolSize,
                                              int maximumPoolSize,
                                              int blockingQueueSize,
                                              int toSubmitTaskNum) {
        


       return -1;
    }

}
