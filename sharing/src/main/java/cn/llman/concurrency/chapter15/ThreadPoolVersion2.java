package cn.llman.concurrency.chapter15;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 对于一个线程池而言，需要包含一下的基本概念：
 * 1. init 初始线程数量
 * 2. active 当前线程池中激活的线程数量
 * 3. max 最大的线程数量
 * 4. size 动态规划中线程池中真实的线程数量
 * 5. 拒绝策略：抛出异常，直接丢弃，阻塞，临时队列
 * 6. 任务队列
 * 7. 线程队列
 *
 * @date 2019/4/11
 */
public class ThreadPoolVersion2 extends Thread {

    /* 一个线程池 核心就是一个保存线程的容器 */
    private final static List<Thread> THREAD_POOL = new ArrayList<>();

    /* 需要一个队列 来保存任务 每个任务都是一个Runnable实例 */
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    private int size; // 线程池中线程的数量
    private int min; // 线程池中最少线程的数量
    private final static int DEFAULT_MIN = 4;
    private int max; // 线程池中最多线程的数量
    private final static int DEFAULT_MAX = 12;
    private int active; // 线程池中激活的数量
    private final static int DEFAULT_ACTIVE = 8;

    private volatile boolean isDestroy = false; // 表示该线程池是否被销毁

    /* 在构造该线程池时，需要在该线程池中存入一定量的线程 */
    public ThreadPoolVersion2() {
        this(DEFAULT_MIN, DEFAULT_MAX, DEFAULT_ACTIVE);
    }

    public ThreadPoolVersion2(int min, int max, int active) {
        this.min = min;
        this.max = max;
        this.active = active;
        init();
    }

    private void init() {
        // 初始化5个线程
        for (int i = 0; i < this.min; i++) {
            createWorkerThread();
        }
    }

    private void createWorkerThread() {
        Thread workThread = new Thread(GROUP, THREAD_PREFIX + (seq++)) {
            private volatile TaskState taskState = TaskState.FREE;

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
                                System.out.println("swallow");
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
        };
        workThread.start();
        THREAD_POOL.add(workThread);
    }

    /**
     * 动态增加或减少线程池中的线程数
     */
    @Override
    public void run() {
        while (!isDestroy) {
            System.out.printf("Pool -> Min:%d,Active:%d,Max:%d,Current:%d,QueueSize:%d \n",
                    this.min, this.active, this.max, this.size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000);
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        createWorkerThread();
                    }
                    System.out.println(">>> The pool increased.");
                    size = active;
                } else if (TASK_QUEUE.size() > max && size < max) {
                    for (int i = size; i < max; i++) {
                        createWorkerThread();
                    }
                    System.out.println(">>> The pool increased to the max load.");
                    size = max;
                }

                synchronized (THREAD_POOL) {
                    if (TASK_QUEUE.isEmpty() && size > active) {
                        System.out.println(">>> try to reduce pool.");
                        int releaseSize = size - active;
                        for (Iterator<Thread> it = THREAD_POOL.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0) break;
                            Thread workThread = it.next();
                            workThread.interrupt();
                            it.remove();
                            releaseSize--;
                        }
                        size = active;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void submit(Runnable runnable) {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(100);
        }

        synchronized (THREAD_POOL) {
            int initVal = THREAD_POOL.size();
            while (initVal > 0) {
                for (Thread task : THREAD_POOL) {
                    task.interrupt();
                    initVal--;
                    Thread.sleep(50);
                }
            }
        }

        System.out.println(GROUP.activeCount());
        this.isDestroy = true;
        System.out.println("The thread pool disposed.");
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

}
