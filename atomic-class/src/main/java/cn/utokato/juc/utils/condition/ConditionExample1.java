package cn.utokato.juc.utils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date 2019/5/23
 */
public class ConditionExample1 {

    private final static ReentrantLock lock = new ReentrantLock();

    private final static Condition condition = lock.newCondition();

    private static int data = 0;

    private static volatile boolean noUse = true;

    private static void buildData() {
        try {
            lock.lock();   // similar to synchronized start # monitor enter
            while (noUse) {
                condition.await(); // similar to monitor.wait();
            }
            data++;
            Optional.of("P: " + data).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
            noUse = true;
            condition.signal(); // similar to monitor.notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // similar to synchronized end # monitor end
        }
    }

    private static void useData() {
        try {
            lock.lock();
            while (!noUse) {
                condition.await();
            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of(Thread.currentThread().getName() + "C: " + data).ifPresent(System.out::println);
            noUse = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 问题：
     * 1. 能否不使用condition，只使用lock
     * -    不能，因为当生产者线程产生数据后，下次会再和消费者一起竞争该lock，如果生产者多次竞争到lock，就会产生数据的重复生成
     * -    可以使用公平锁来解决这个问题，但不能从根本上消除这个问题
     * <p>
     * 2. 一个线程获取lock后，调用了await方法，陷入了阻塞；另一个线程为什么还可以获取lock
     * -    调用了await方法后，会自动释放锁
     * <p>
     * 3. 是否可以不使用lock，只使用condition
     * -    不能，会抛出IllegalMonitorStateException
     * -    condition await() 必须先获取与之对应的lock
     */
    public static void main(String[] args) {
        new Thread(() -> {
            do {
                buildData();
            } while (true);
        }).start();

        new Thread(() -> {
            do {
                useData();
            } while (true);
        }).start();
    }
}
