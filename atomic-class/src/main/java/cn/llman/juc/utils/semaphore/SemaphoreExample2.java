package cn.llman.juc.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/20
 */
public class SemaphoreExample2 {

    /**
     * 应用场景：
     * 有一个固定大小的连接池，当请求连接数量大于size时，需要执行什么样的策略：
     * 1. 等待一段时间，还是不能获取连接，抛出异常
     * 2. 直接进入阻塞
     * 3. 丢弃多余连接，什么处理也不做
     * 4. 直接抛出异常
     * 5. 在线程池中注册一个回调函数，当线程池中有空闲的线程时，去通知调用者发起连接
     */
    public static void main(String[] args) {

        final Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " in.");
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " get the semaphore.");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + " out.");
                }

            }).start();
        }

        while (true) {
            try {
                System.out.println("AvailablePermits: " + semaphore.availablePermits()); // 当前能够使用的permit数量
                System.out.println("QueueLength: " + semaphore.getQueueLength()); // 获取不到permit而进入阻塞队列中的线程数量
                System.out.println("=======================");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
