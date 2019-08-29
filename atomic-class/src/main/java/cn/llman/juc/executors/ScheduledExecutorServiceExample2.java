package cn.llman.juc.executors;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @date 2019/5/29
 */
public class ScheduledExecutorServiceExample2 {
    public static void main(String[] args) {
        // testScheduleWithFixedDelay();
        test();
    }

    /**
     * 固定延时时间去执行任务
     * <p>
     * 不论该任务执行耗时多长时间，都会在该任务执行完毕后，再延时delay的时间再次执行该任务
     */
    private static void testScheduleWithFixedDelay() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        final AtomicLong interval = new AtomicLong();
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() -> {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                if (interval.get() == 0) {
                    System.out.printf("First to invoke at %d .\n", currentTimeMillis);
                } else {
                    System.out.printf("Invoked spend [ %d ] .\n", currentTimeMillis - interval.get());
                }
                interval.set(currentTimeMillis);
                TimeUnit.SECONDS.sleep(6);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    /**
     * {@link ScheduledThreadPoolExecutor#setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean)}
     * 默认为false，即调用了shutdown后，周期任务就会停止
     * 改为true后，即使调用了shutdown，周期任务也会继续执行
     * <p>
     * {@link ScheduledThreadPoolExecutor#setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean)}
     * 默认为true，调用了shutdown后，周期任务会继续执行 ? 有问题
     */
    private static void test() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
        final AtomicLong interval = new AtomicLong();
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() -> {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                if (interval.get() == 0) {
                    System.out.printf("First to invoke at %d .\n", currentTimeMillis);
                } else {
                    System.out.printf("Invoked spend [ %d ] .\n", currentTimeMillis - interval.get());
                }
                interval.set(currentTimeMillis);
                TimeUnit.SECONDS.sleep(6);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        System.out.println("===over===");
    }
}
