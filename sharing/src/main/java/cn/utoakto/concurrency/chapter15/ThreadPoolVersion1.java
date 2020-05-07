package cn.utoakto.concurrency.chapter15;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @date 2019/4/11
 */
public class ThreadPoolVersion1 {

    /* 一个线程池 核心就是一个保存线程的容器 */
    private final static List<Thread> THREAD_POOL = new ArrayList<>();

    /* 需要一个队列 来保存任务 每个任务都是一个Runnable实例 */
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    /* 在构造该线程池时，需要在该线程池中存入一定量的线程 */
    public ThreadPoolVersion1() {
        init();
    }

    private void init() {
        // 初始化5个线程
        for (int i = 0; i < 5; i++) {
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
                                System.out.println();
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

    void submit(Runnable runnable) {
        synchronized (TASK_QUEUE) {
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

}
