package cn.llman.concurrency.chapter13;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 简易版 线程池
 * <p>
 * 自定义简单线程池
 * <p>
 * 对于一个线程池而言，需要包含一下的基本概念：
 * 1. init 初始线程数量
 * 2. active 当前线程池中激活的线程数量
 * 3. max 最大的线程数量
 * 4. size 动态规划中线程池中真实的线程数量
 * 5. 拒绝策略：抛出异常，直接丢弃，阻塞，临时队列
 * 6. 任务队列
 * 7. 线程队列
 *
 * <b>很重要</b>
 *
 * @date 2019/4/10
 */
public class SimpleThreadPool {

    private final int size;

    private final static int DEFAULT_SIZE = 10;

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    /**
     * 任务队列
     */
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    /**
     * 线程队列
     */
    private final static List<Worker> THREAD_QUEUE = new ArrayList<>();

    public SimpleThreadPool() {
        this(DEFAULT_SIZE);
    }

    public SimpleThreadPool(int size) {
        this.size = size;
        init();
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            createWorkerTask();
        }
    }

    public void submit(Runnable runnable) {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    private void createWorkerTask() {
        Worker worker = new Worker(GROUP, THREAD_PREFIX + (seq++));
        worker.start();
        THREAD_QUEUE.add(worker);
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

    private static class Worker extends Thread {

        private volatile TaskState taskState = TaskState.FREE;


        public Worker(ThreadGroup group, String name) {
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
                            e.printStackTrace();
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


    public static void main(String[] args) {
        SimpleThreadPool pool = new SimpleThreadPool();
        IntStream.rangeClosed(0, 40)
                .forEach(i -> pool.submit(() -> {
                    System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " start.");
                    try {
                        Thread.sleep(10_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("The runnable " + i + " be service by " + Thread.currentThread() + " finished!");
                }));
    }
}
