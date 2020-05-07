package cn.utokato.juc.executors;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @date 2019/5/28
 */
public class ExecutorServiceExample1 {

    /**
     * {@link ExecutorService}
     */
    public static void main(String[] args) throws InterruptedException {
        // isShutdown();
        // isTerminated();
        // executeRunnableError();
        executeRunnableTask();
    }

    /**
     * {@link ExecutorService#isShutdown()}
     * <p>
     * 如果在线程池关闭以后，继续execute任务，会抛出RejectedExecutionException
     */
    private static void isShutdown() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> nap(5));

        Optional.of(executorService.isShutdown()).ifPresent(System.out::println);
        executorService.shutdown();
        Optional.of(executorService.isShutdown()).ifPresent(System.out::println);
        executorService.execute(() -> System.out.println("I can't exec cause to the ExecutorService is shutdown"));
    }

    /**
     * {@link ExecutorService#isTerminated()}
     * {@link ThreadPoolExecutor#isTerminating()}
     */
    private static void isTerminated() {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        service.execute(() -> nap(2));
        service.shutdown();
        System.out.println("isShutdown: " + service.isShutdown());
        System.out.println("isTerminated: " + service.isTerminated());
        System.out.println("isTerminating: " + service.isTerminating());
    }

    /**
     * {@link MyThreadFactory}
     * 通过在自定义的 {@link ThreadFactory} 中设置 thread的setUncaughtExceptionHandler方法
     * 可以在该线程执行出现异常时，进行回调。
     * 但这种方法只能将异常的堆栈信息和当前的thread信息获取到，无法获取到当前正在执行任务的信息
     */
    private static void executeRunnableError() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10, new MyThreadFactory());
        IntStream.range(0, 10).boxed().forEach(i -> service.execute(() -> System.out.println(1 / 0)));
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }

    private static class MyThreadFactory implements ThreadFactory {

        private final static AtomicInteger SEQ = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("My-Thread-" + SEQ.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, e) -> {
                System.out.println("The thread " + t.getName() + " execute failed.");
                e.printStackTrace();
                System.out.println("===============================");
            });
            return thread;
        }
    }

    /**
     * {@link MyTask}
     * 自定义任务来包装{@link Runnable}
     * 通过模板方法的设计模式，在自定义任务类中定义该任务的抽象执行逻辑
     * 以及，出现了异常时的处理逻辑
     */
    private static void executeRunnableTask() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).boxed().forEach(i -> service.execute(
                new MyTask(i) {
                    @Override
                    protected void error(Throwable e) {
                        System.out.println("The num: " + i + " failed, update status to Error.");
                    }

                    @Override
                    protected void done() {
                        System.out.println("The num: " + i + " successful, update status to Success.");
                    }

                    @Override
                    protected void doExecute() {
                        if (i % 3 == 0) {
                            int temp = 1 / 0;
                        }
                    }

                    @Override
                    protected void doInit() {
                        // do nothing
                    }
                }
        ));
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("============================");
    }

    private abstract static class MyTask implements Runnable {

        protected final int num;

        private MyTask(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            try {
                this.doInit();
                this.doExecute();
                this.done();
            } catch (Throwable e) {
                this.error(e);
            }
        }

        protected abstract void error(Throwable e);

        protected abstract void done();

        protected abstract void doExecute();

        protected abstract void doInit();

    }


    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
