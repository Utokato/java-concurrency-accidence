package cn.utokato.juc.utils.semaphore;

import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/20
 */
public class SemaphoreExample4 {
    public static void main(String[] args) throws InterruptedException {
        final MySemaphore semaphore = new MySemaphore(5);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.drainPermits();
                System.out.println(semaphore.availablePermits());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(5);
            }
            System.out.println("T1 finished!");
        });

        t1.start();

        TimeUnit.MILLISECONDS.sleep(10);

        Thread t2 = new Thread(() -> {
            try {
                System.out.println(semaphore.tryAcquire() ? "Get semaphore successful" : "Get semaphore failure");
                System.out.println(semaphore.availablePermits());
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            System.out.println("T2 finished!");
        });

        t2.start();

        TimeUnit.MILLISECONDS.sleep(20);
        System.out.println(semaphore.hasQueuedThreads());

        Collection<Thread> waitingThreads = semaphore.getAllWaitingThreads();
        waitingThreads.forEach(System.out::println);


    }

    static class MySemaphore extends Semaphore {

        public MySemaphore(int permits) {
            super(permits);
        }

        public MySemaphore(int permits, boolean fair) {
            super(permits, fair);
        }

        public Collection<Thread> getAllWaitingThreads() {
            return super.getQueuedThreads();
        }
    }
}
