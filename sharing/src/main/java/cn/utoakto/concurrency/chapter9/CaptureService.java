package cn.utoakto.concurrency.chapter9;

import java.util.*;
import java.util.stream.Stream;

/**
 * 模拟多线程同时处理10台机器上的业务
 * 同时只能有5个线程工作
 * <p>
 * 如果引入线程池的观念，会更加容易控制
 *
 * @date 2019/4/9
 */
public class CaptureService {

    final static private LinkedList<Object> WORKER_QUEUE = new LinkedList<>();
    final static private int MAX_WORKER = 5;

    public static void main(String[] args) {
        List<Thread> workers = new ArrayList<>();
        Stream.of("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
                .map(CaptureService::createCaptureThread)
                .forEach(t -> {
                    workers.add(t);
                    t.start();
                });

        workers.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Optional.of("All of capture work finished.").ifPresent(System.out::println);

    }


    /**
     * 设置 WORKER_QUEUE 中最多存放5个对象
     * 超过5个，便进入wait状态
     */
    private static Thread createCaptureThread(String threadName) {
        return new Thread(() -> {
            Optional.of("The worker [" + Thread.currentThread().getName() + "] BEGIN to capture data.")
                    .ifPresent(System.out::println);
            synchronized (WORKER_QUEUE) {
                while (WORKER_QUEUE.size() >= MAX_WORKER) {
                    try {
                        WORKER_QUEUE.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                WORKER_QUEUE.addLast(threadName);
            }

            Optional.of("The worker [" + Thread.currentThread().getName() + "] is working.")
                    .ifPresent(System.out::println);
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (WORKER_QUEUE) {
                Optional.of("The worker [" + Thread.currentThread().getName() + "] END capture data.")
                        .ifPresent(System.out::println);
                WORKER_QUEUE.removeFirst();
                WORKER_QUEUE.notifyAll();
            }
        }, threadName);
    }
}
