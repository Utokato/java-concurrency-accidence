package cn.llman.juc.collections.concurrency;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lma
 * @date 2019/06/13
 */
public class ConcurrentListPerformanceTest {

    /**
     * ---start---
     * SynchronizedRandomAccessList
     * Thread num: 10, duration time: 6 ms.
     * Thread num: 20, duration time: 5 ms.
     * Thread num: 30, duration time: 5 ms.
     * Thread num: 40, duration time: 5 ms.
     * Thread num: 50, duration time: 6 ms.
     * Thread num: 60, duration time: 7 ms.
     * Thread num: 70, duration time: 6 ms.
     * Thread num: 80, duration time: 6 ms.
     * Thread num: 90, duration time: 5 ms.
     * Thread num: 100, duration time: 5 ms.
     * ---end---
     * <p>
     * ---start---
     * ConcurrentLinkedQueue
     * Thread num: 10, duration time: 49 ms.
     * Thread num: 20, duration time: 5 ms.
     * Thread num: 30, duration time: 5 ms.
     * Thread num: 40, duration time: 5 ms.
     * Thread num: 50, duration time: 5 ms.
     * Thread num: 60, duration time: 5 ms.
     * Thread num: 70, duration time: 5 ms.
     * Thread num: 80, duration time: 5 ms.
     * Thread num: 90, duration time: 5 ms.
     * Thread num: 100, duration time: 5 ms.
     * ---end---
     * <p>
     * ---start---
     * CopyOnWriteArrayList
     * Thread num: 10, duration time: 952 ms.
     * Thread num: 20, duration time: 697 ms.
     * Thread num: 30, duration time: 696 ms.
     * Thread num: 40, duration time: 696 ms.
     * Thread num: 50, duration time: 758 ms.
     * Thread num: 60, duration time: 881 ms.
     * Thread num: 70, duration time: 791 ms.
     * Thread num: 80, duration time: 771 ms.
     * Thread num: 90, duration time: 748 ms.
     * Thread num: 100, duration time: 785 ms.
     * ---end---
     */
    public static void main(String[] args) throws InterruptedException {
        for (int i = 10; i <= 100; i += 10) {
            pressureTest(new ConcurrentLinkedQueue<>(), i, true);
            pressureTest(new CopyOnWriteArrayList<>(), i, true);
            pressureTest(Collections.synchronizedList(new ArrayList<>()), i, true);
        }

        summary.forEach((k, v) -> {
            System.out.println("---start---");
            System.out.println(k);
            v.forEach(System.out::println);
            System.out.println("---end---");
        });

    }


    static class Entry {
        int threadNum;
        long duration;

        public Entry(int threadNum, long duration) {
            this.threadNum = threadNum;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "Thread num: " + threadNum + ", duration time: " + duration + " ms.";
        }
    }

    private final static Map<String, List<Entry>> summary = new HashMap<>();


    private static void pressureTest(final Collection<String> collection, int threadNum, boolean hasWrite) throws InterruptedException {
        System.out.println("Start pressure testing for map [" + collection.getClass() + "] use " + threadNum + " threads, and write is " + hasWrite);
        long totalTime = 0L;
        final int MAX_DATA = 50000;
        for (int i = 0; i < 5; i++) {
            final AtomicInteger counter = new AtomicInteger(0);
            collection.clear();
            long startTime = System.nanoTime();
            ExecutorService service = Executors.newFixedThreadPool(threadNum);

            service.execute(() -> {
                for (int k = 0; k < MAX_DATA && counter.getAndIncrement() < MAX_DATA; k++) {
                    Integer temp = (int) Math.ceil(Math.random() * 600000);
                    collection.add(String.valueOf(temp)); // 模拟写操作
                }
            });

            service.shutdown();
            service.awaitTermination(2, TimeUnit.HOURS);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000L;
            System.out.println(MAX_DATA + " elements inserted/retrieved in " + duration + " ms");
            System.out.println("The counter is: " + counter);
            totalTime += duration;
        }

        List<Entry> entries = summary.computeIfAbsent(collection.getClass().getSimpleName(), k -> new ArrayList<>());
        entries.add(new Entry(threadNum, (totalTime / 5)));


        System.out.println("For the map [" + collection.getClass() + "] average time is: " + (totalTime / 5) + " ms");
    }
}
