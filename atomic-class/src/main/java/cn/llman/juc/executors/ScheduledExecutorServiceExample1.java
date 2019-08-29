package cn.llman.juc.executors;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @date 2019/5/29
 */
public class ScheduledExecutorServiceExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        ScheduledFuture<?> future = executor.schedule(() -> System.out.println("I will be invoked."), 2, TimeUnit.SECONDS);

        boolean isCancel = future.cancel(true);
        System.out.println(isCancel);
        System.out.println("================");

        ScheduledFuture<Integer> future1 = executor.schedule(() -> 2, 2, TimeUnit.SECONDS);
        System.out.println(future1.get());

        System.out.println("==============");

        // ScheduledFuture<?> future2 = executor.scheduleAtFixedRate(() -> System.out.println("I am running." + System.currentTimeMillis()), 2, 2, TimeUnit.SECONDS);

        System.out.println("=================");


        testScheduleAtFixedRate();

    }

    /**
     * 定时任务的两种情形：
     * 当任务耗时比定时时间长时，不同框架的处理逻辑不同：
     * 1. crontab/quartz/control-M : 以定时时间为准，当定时时间到了，当前的任务没有执行完毕，会启动行的进程或线程来再次执行新的任务
     * 2. java.timer：以任务时间为准，只有当前任务执行完毕后，定时任务才会继续出发。这种情况下定时任务的周期会受到任务时长的影响
     */
    private static void testScheduleAtFixedRate() {
        final AtomicLong interval = new AtomicLong();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                if (interval.get() == 0) {
                    System.out.printf("First to invoke at %d .\n", currentTimeMillis);
                } else {
                    System.out.printf("Invoked spend [ %d ] .\n", currentTimeMillis - interval.get());
                }
                interval.set(currentTimeMillis);
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, 0, 2, TimeUnit.SECONDS);
    }


}
