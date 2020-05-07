package cn.utokato.juc.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date 2019/5/29
 */
public class OneProblemExample {
    public static void main(String[] args) throws Exception {
        // dealWithExecutorService();
        // dealWithExecutorCompletionService();
        // problem1();
        // dealWithMyTask();

    }

    /**
     * 为了解决{@link ExecutorCompletionService}的不足，我们可以自定义任务类来实现会未完成任务的分析
     * {@link MyTask} 实现 {@link Callable} 接口
     * 在其中{@link MyTask#success}设置一个flag开关，只有当任务正常执行时，该flag才为true
     * 然后，我们需要分析哪些任务没有完成时，通过该flag进行过滤就可以到达目的
     */
    private static void dealWithMyTask() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        List<MyTask> myTasks = IntStream.range(0, 5).boxed().map(MyTask::new).collect(Collectors.toList());
        final ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);

        myTasks.forEach(completionService::submit);

        TimeUnit.SECONDS.sleep(12);

        executorService.shutdownNow();

        myTasks.stream().filter(myTask -> !myTask.isSuccess()).forEach(System.out::println);
    }

    private static class MyTask implements Callable<Integer> {

        private final int value;

        private boolean success = false;

        MyTask(int value) {
            this.value = value;
        }

        @Override
        public Integer call() throws Exception {
            System.out.printf("Task [%d] will be executed. \n", value);
            TimeUnit.SECONDS.sleep(value * 5 + 10);
            System.out.printf("Task [%d] is done. \n", value);
            success = true;
            return value;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    /**
     * 当执行一段时间后，我们需要关闭线程池服务，并获取没有完成的任务进行分析或再处理，此时ExecutorCompletionService就不能很好的解决这个问题
     * 这是由于当执行了shutdownNow会打断正在执行的线程，并且该线程执行的任务不会返回
     * 同时{@link ExecutorService#shutdownNow()}返回的List<Runnable>中的任务是经过私有内部类包装的Runnable，已经不是我们自己提交的任务了
     * 我们无法对这个包装的任务进行类型转换，也就不能继续分析和再处理了
     */
    private static void problem1() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        List<Runnable> runnableList = IntStream.range(0, 5).boxed().map(OneProblemExample::toTask).collect(Collectors.toList());

        final ExecutorCompletionService<Object> completionService = new ExecutorCompletionService<>(executorService);

        runnableList.forEach(r -> completionService.submit(r, null));

        TimeUnit.SECONDS.sleep(12);

        List<Runnable> runnables = executorService.shutdownNow();
        System.out.println(runnables);
    }

    /**
     * {@link ExecutorCompletionService} 解决了 {@link ExecutorService} 的缺陷
     * 通过 {@link ExecutorCompletionService#take()} 可以获取最快执行完任务的future，通过该future的get方法就可以获取最快完成任务的返回结果
     * ExecutorCompletionService的关注点在于已经完成的任务。对于出现异常的任务，或者一定时段内没有完成任务没有很好的处理
     */
    private static void dealWithExecutorCompletionService() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Runnable> runnableList = IntStream.range(0, 5).boxed().map(OneProblemExample::toTask).collect(Collectors.toList());

        final ExecutorCompletionService<Object> completionService = new ExecutorCompletionService<>(executorService);

        runnableList.forEach(r -> completionService.submit(r, null));

        Future<?> future;

        while ((future = completionService.take()) != null) {
            System.out.println(future.get());
        }

    }

    /**
     * {@link ExecutorService} 的缺陷
     * ExecutorService 中执行的每个任务，在完成后都会返回一个future，通过future的get方法可以获取该任务的返回结果
     * 如果不巧的选择了一个最慢的任务的future，并通过该future的get方法去获取执行结果
     * 由于future的get方法是一个阻塞方法，回导致所有的线程都被最慢future的get方法阻塞
     * 即使之前有些线程已经执行完毕了，但由于阻塞，不能直接获取已经执行完成任务的返回结果
     */
    private static void dealWithExecutorService() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Runnable> runnableList = IntStream.range(0, 6).boxed().map(OneProblemExample::toTask).collect(Collectors.toList());

        ArrayList<Future> futures = new ArrayList<>();

        runnableList.forEach(r -> futures.add(executorService.submit(r)));

        futures.get(4).get();
        System.out.println("===4===");
        futures.get(3).get();
        System.out.println("===3===");
        futures.get(2).get();
        System.out.println("===2===");
        futures.get(1).get();
        System.out.println("===1===");
        futures.get(0).get();
        System.out.println("===0===");
    }

    private static Runnable toTask(int i) {
        return () -> {

            try {
                System.out.printf("Task [%d] will be executed. \n", i);
                TimeUnit.SECONDS.sleep(i * 5 + 10);
                System.out.printf("Task [%d] is done. \n", i);
            } catch (InterruptedException e) {
                System.out.printf("Task [%d] is interrupted. \n", i);
            }

        };
    }


}
