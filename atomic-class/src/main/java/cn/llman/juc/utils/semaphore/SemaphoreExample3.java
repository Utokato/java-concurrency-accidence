package cn.llman.juc.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * {@link Semaphore#acquire(int)} int 表示本次获取几个信号量
 * {@link Semaphore#release(int)} int 表示本次释放几个信号量
 * {@link Semaphore#availablePermits()} 当前可用的信号量的数量
 * {@link Semaphore#getQueueLength()} 由于获取不到信号量而进入阻塞的线程的数量
 * {@link Semaphore#getQueuedThreads()} 由于获取不到信号量而进入阻塞的线程
 * {@link Semaphore#acquireUninterruptibly()} 获取信号量，即使进入阻塞状态也不能被中断
 *
 * @date 2019/5/20
 */
public class SemaphoreExample3 {
    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            System.out.println("T1 finished!");
        });

        t1.start();

        TimeUnit.MILLISECONDS.sleep(50);


        /**
         * {@link Semaphore#acquire()} 当线程被打断时，会抛出InterruptedException
         * {@link Semaphore#acquireUninterruptibly()} 当线程被打断时，不会抛出异常
         */
        Thread t2 = new Thread(() -> {
            try {
                semaphore.acquireUninterruptibly();
                // TimeUnit.SECONDS.sleep(5);
            } /*catch (InterruptedException e) {
                e.printStackTrace();
            }*/ finally {
                semaphore.release();
            }
            System.out.println("T2 finished!");
        });

        t2.start();

        TimeUnit.MILLISECONDS.sleep(50);
        t2.interrupt();
    }
}
