package cn.llman.concurrency.chapter13;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 线程池 + 拒绝策略 + 关闭线程池
 * <p>
 * // 存在一个问题：todo 当提交的任务数量大于了当前的任务队列的大小，根据默认的抛弃策略会抛出异常，抛出异常后，线程池不能正常关闭
 *
 * @date 2019/4/10
 */
public class SimpleThreadPool2 {

    /**
     * 线程池大小
     */
    private final int size;

    /**
     * 任务队列大小
     */
    private final int taskQueueSize;

    /**
     * 默认线程池大小
     */
    private final static int DEFAULT_SIZE = 10;

    /**
     * 默认任务队列大小
     */
    private final static int DEFAULT_TASK_QUEUE_SIZE = 2000;

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    private final DiscardPolicy discardPolicy;

    public final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard this task cause queue is full.");
    };

    /**
     * 任务队列
     */
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 线程队列
     */
    private final static List<WorkerTask> THREAD_QUEUE = new ArrayList<>();

    private volatile boolean destroy = false;

    public SimpleThreadPool2() {
        this(DEFAULT_SIZE, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    public SimpleThreadPool2(int size, int taskQueueSize, DiscardPolicy discardPolicy) {
        this.size = size;
        this.taskQueueSize = taskQueueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            createWorkerTask();
        }
    }

    public void submit(Runnable runnable) {
        if (destroy) throw new IllegalStateException("The thread pool is destroyed and not allow to submit task.");

        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() >= taskQueueSize) {
                discardPolicy.discard();
            }
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(100);
        }

        int initVal = THREAD_QUEUE.size();
        while (initVal > 0) {
            for (WorkerTask task : THREAD_QUEUE) {
                if (task.getTaskState() == TaskState.BLOCKED) {
                    task.interrupt();
                    task.close();
                    initVal--;
                } else {
                    Thread.sleep(50);
                }
            }
        }

        this.destroy = true;
        System.out.println("The thread pool disposed.");
    }

    public int getSize() {
        return size;
    }

    public int getTaskQueueSize() {
        return taskQueueSize;
    }

    public boolean destroy() {
        return this.destroy;
    }


    private void createWorkerTask() {
        WorkerTask task = new WorkerTask(GROUP, THREAD_PREFIX + (seq++));
        task.start();
        THREAD_QUEUE.add(task);
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    public interface DiscardPolicy {
        void discard() throws DiscardException;
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

    private static class WorkerTask extends Thread {

        private volatile TaskState taskState = TaskState.FREE;


        public WorkerTask(ThreadGroup group, String name) {
            super(group, name);
        }

        @Override
        public void run() {
            OUTER:
            while (this.taskState != TaskState.DEAD) {
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    taskState = TaskState.FREE;
                }

            }
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        public void close() {
            this.taskState = TaskState.DEAD;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // SimpleThreadPool2 pool = new SimpleThreadPool2(6, 2000, SimpleThreadPool2.DEFAULT_DISCARD_POLICY);
        SimpleThreadPool2 pool = new SimpleThreadPool2(6, 10, () -> {
        });
        IntStream.rangeClosed(0, 20)
                .forEach(i -> pool.submit(() -> {
                    System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " start.");
                    try {
                        Thread.sleep(1_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " finished!");
                }));

        Thread.sleep(10_000);

        pool.shutdown();
    }
}
