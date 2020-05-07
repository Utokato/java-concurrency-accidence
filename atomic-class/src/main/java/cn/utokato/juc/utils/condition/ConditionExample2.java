package cn.utokato.juc.utils.condition;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @date 2019/5/23
 */
public class ConditionExample2 {

    private final static ReentrantLock LOCK = new ReentrantLock();

    private final static Condition PRODUCE_CONDITION = LOCK.newCondition();
    private final static Condition CONSUME_CONDITION = LOCK.newCondition();

    private final static LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();

    private final static int MAX_CAPACITY = 100;

    public static void main(String[] args) {
        IntStream.rangeClosed(0, 9).boxed().forEach(ConditionExample2::beginProduce);
        IntStream.rangeClosed(0, 4).boxed().forEach(ConditionExample2::beginConsume);
    }

    private static void beginProduce(int i) {
        new Thread(() -> {
            for (; ; ) {
                produce();
                sleep(2L);
            }
        }, "P-" + i).start();
    }

    private static void beginConsume(int i) {
        new Thread(() -> {
            for (; ; ) {
                consume();
                sleep(3L);
            }
        }, "C-" + i).start();
    }

    private static void produce() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.size() >= MAX_CAPACITY) {
                PRODUCE_CONDITION.await();
            }

            System.out.println("LOCK.getWaitQueueLength(PRODUCE_CONDITION): " + LOCK.getWaitQueueLength(PRODUCE_CONDITION));
            System.out.println("LOCK.getWaitQueueLength(CONSUME_CONDITION): " + LOCK.getWaitQueueLength(CONSUME_CONDITION));
            System.out.println("LOCK.hasWaiters(PRODUCE_CONDITION): " + LOCK.hasWaiters(PRODUCE_CONDITION));
            System.out.println("LOCK.hasWaiters(CONSUME_CONDITION): " + LOCK.hasWaiters(CONSUME_CONDITION));

            Long value = System.currentTimeMillis();
            Optional.of(Thread.currentThread().getName() + " put: " + value).ifPresent(System.out::println);
            TIMESTAMP_POOL.addLast(value);

            CONSUME_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static void consume() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUME_CONDITION.await();
            }
            Long result = TIMESTAMP_POOL.removeFirst();
            Optional.of(Thread.currentThread().getName() + " get: " + result).ifPresent(System.out::println);
            PRODUCE_CONDITION.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private static void sleep(Long sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
