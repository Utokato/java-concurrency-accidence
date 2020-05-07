package cn.utokato.juc.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @date 2019/5/20
 */
public class SemaphoreExample1 {

    public static void main(String[] args) {

        final SemaphoreLock lock = new SemaphoreLock();
        IntStream.rangeClosed(1, 2).forEach(i -> new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " running.");
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " get the SemaphoreLock");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " release the SemaphoreLock");
            }

        }).start());

    }

    static class SemaphoreLock {
        private final Semaphore semaphore = new Semaphore(1);

        public void lock() throws InterruptedException {
            semaphore.acquire();
        }

        public void unlock() {
            semaphore.release();
        }
    }
}
