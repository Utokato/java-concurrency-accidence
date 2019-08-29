package cn.llman.juc.executors;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @date 2019/5/28
 */
public class ExecutorServiceExample3 {

    public static void main(String[] args) {
        // test();
        // testAllowsCoreThreadTimeOut();
        // testRemove();
        // testPrestartCoreThread();
        // testPrestartAllCoreThreads();
        testBeforeExecuteAndAfterExecute();

    }

    private static void test() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        System.out.println(threadPool.getActiveCount());
        threadPool.execute(() -> {
            nap(10);
        });

        nap(1);
        System.out.println(threadPool.getActiveCount());

    }

    /**
     * {@link ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)}
     */
    private static void testAllowsCoreThreadTimeOut() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        threadPool.setKeepAliveTime(20L, TimeUnit.SECONDS);
        threadPool.allowCoreThreadTimeOut(true); // 允许核心线程被回收
        IntStream.range(0, 5).forEach(i -> threadPool.execute(() -> nap(10)));

    }

    /**
     * {@link ThreadPoolExecutor#remove(Runnable)}
     */
    private static void testRemove() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        threadPool.setKeepAliveTime(20L, TimeUnit.SECONDS);
        threadPool.allowCoreThreadTimeOut(true); // 允许核心线程被回收
        IntStream.range(0, 2).forEach(i -> threadPool.execute(() -> {
            nap(10);
            System.out.println("I am finished.");
        }));
        nap(1);
        Runnable runnable = () -> {
            System.out.println("I can't be exec");
        };
        threadPool.execute(runnable);
        threadPool.remove(runnable); // 从线程池的任务队列中移除该任务后，该任务就不会继续被执行
        // 不能移除正在执行的任务
    }

    /**
     * {@link ThreadPoolExecutor#prestartCoreThread()}
     * 预先启动一个核心线程
     */
    private static void testPrestartCoreThread() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        boolean b = threadPool.prestartCoreThread();
        // 多次调用ThreadPoolExecutor#prestartCoreThread()，如果当前的线程池中有空闲的线程，就不会继续激活线程
        // 多次调用ThreadPoolExecutor#prestartCoreThread()，最终激活核心线程的数量 不会 大于预设的核心线程数量
    }

    /**
     * {@link ThreadPoolExecutor#prestartAllCoreThreads()}
     * 预先启动所有的核心线程
     */
    private static void testPrestartAllCoreThreads() {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        int allCoreThreads = threadPool.prestartAllCoreThreads();
        System.out.println(allCoreThreads);
    }

    /**
     * {@link ThreadPoolExecutor#afterExecute(Runnable, Throwable)}
     * {@link ThreadPoolExecutor#beforeExecute(Thread, Runnable)}
     *
     * 类似于 Spring 中的AOP
     *
     */
    private static void testBeforeExecuteAndAfterExecute() {
        ThreadPoolExecutor executor = new MyThreadPoolExecutor(
                1,
                2,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Thread::new,
                new ThreadPoolExecutor.AbortPolicy()
        );

        /*executor.execute(new MyRunnable(1) {
            @Override
            public void run() {
                System.out.println("================");
            }
        });*/

        executor.execute(new MyRunnable(1) {
            @Override
            public void run() {
                System.out.println("=============");
                System.out.println(1/0);
            }
        });


    }

    private abstract static class MyRunnable implements Runnable {
        private final int num;

        protected MyRunnable(int num) {
            this.num = num;
        }

        protected int getData() {
            return num;
        }
    }

    private static class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize,
                                    int maximumPoolSize,
                                    long keepAliveTime,
                                    TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue,
                                    ThreadFactory threadFactory,
                                    RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("Initial -> " + ((MyRunnable) r).getData());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (Objects.isNull(t)) {
                System.out.println("Finished -> " + ((MyRunnable) r).getData());
            } else {
                t.printStackTrace();
            }
        }
    }

    /**
     * To sleep with second
     *
     * @param sec
     */
    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
