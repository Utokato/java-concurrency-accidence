package cn.utokato.juc.collections.concurrency;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lma
 * @date 2019/06/09
 */
public class JDKMapPerformance {
    public static void main(String[] args) throws InterruptedException {
        runWithMultiThreads();
        // runWithSingleThread();

    }

    /**
     * Start pressure testing for map [class java.util.Hashtable] use 1 threads, and write is false
     * For the map [class java.util.Hashtable] average time is: 1720 ms
     * Start pressure testing for map [class java.util.Hashtable] use 1 threads, and write is true
     * For the map [class java.util.Hashtable] average time is: 1373 ms
     * -------------
     * Start pressure testing for map [class java.util.HashMap] use 1 threads, and write is false
     * For the map [class java.util.HashMap] average time is: 1069 ms
     * Start pressure testing for map [class java.util.HashMap] use 1 threads, and write is true
     * For the map [class java.util.HashMap] average time is: 1244 ms
     * -------------
     * Start pressure testing for map [class java.util.Collections$SynchronizedMap] use 1 threads, and write is false
     * For the map [class java.util.Collections$SynchronizedMap] average time is: 1149 ms
     * Start pressure testing for map [class java.util.Collections$SynchronizedMap] use 1 threads, and write is true
     * For the map [class java.util.Collections$SynchronizedMap] average time is: 1186 ms
     * -------------
     * Start pressure testing for map [class java.util.concurrent.ConcurrentHashMap] use 1 threads, and write is false
     * For the map [class java.util.concurrent.ConcurrentHashMap] average time is: 983 ms
     * Start pressure testing for map [class java.util.concurrent.ConcurrentHashMap] use 1 threads, and write is true
     * For the map [class java.util.concurrent.ConcurrentHashMap] average time is: 1267 ms
     */
    private static void runWithSingleThread() throws InterruptedException {
        pressureTest(new Hashtable<>(), 1, false);
        pressureTest(new Hashtable<>(), 1, true);

        System.out.println("-------------");

        pressureTest(new HashMap<>(), 1, false);
        pressureTest(new HashMap<>(), 1, true);

        System.out.println("-------------");

        pressureTest(Collections.synchronizedMap(new HashMap<>()), 1, false);
        pressureTest(Collections.synchronizedMap(new HashMap<>()), 1, true);

        System.out.println("-------------");

        pressureTest(new ConcurrentHashMap<>(), 1, false);
        pressureTest(new ConcurrentHashMap<>(), 1, true);
    }


    /**
     * Start pressure testing for map [class java.util.Hashtable] use 5 threads, and write is false
     * For the map [class java.util.Hashtable] average time is: 1324 ms
     * Start pressure testing for map [class java.util.Hashtable] use 5 threads, and write is true
     * For the map [class java.util.Hashtable] average time is: 1697 ms
     * -------------
     * Start pressure testing for map [class java.util.Collections$SynchronizedMap] use 5 threads, and write is false
     * For the map [class java.util.Collections$SynchronizedMap] average time is: 1085 ms
     * Start pressure testing for map [class java.util.Collections$SynchronizedMap] use 5 threads, and write is true
     * For the map [class java.util.Collections$SynchronizedMap] average time is: 1725 ms
     * -------------
     * Start pressure testing for map [class java.util.concurrent.ConcurrentHashMap] use 5 threads, and write is false
     * For the map [class java.util.concurrent.ConcurrentHashMap] average time is: 488 ms
     * Start pressure testing for map [class java.util.concurrent.ConcurrentHashMap] use 5 threads, and write is true
     * For the map [class java.util.concurrent.ConcurrentHashMap] average time is: 564 ms
     */
    private static void runWithMultiThreads() throws InterruptedException {
        pressureTest(new Hashtable<>(), 5, false);
        pressureTest(new Hashtable<>(), 5, true);

        System.out.println("-------------");

        pressureTest(Collections.synchronizedMap(new HashMap<>()), 5, false);
        pressureTest(Collections.synchronizedMap(new HashMap<>()), 5, true);

        System.out.println("-------------");

        pressureTest(new ConcurrentHashMap<>(), 5, false);
        pressureTest(new ConcurrentHashMap<>(), 5, true);
    }

    private static void pressureTest(final Map<String, Integer> map, int threadNum, boolean hasWrite) throws InterruptedException {
        System.out.println("Start pressure testing for map [" + map.getClass() + "] use " + threadNum + " threads, and write is " + hasWrite);
        long totalTime = 0L;
        final int MAX_DATA = 250_0000;
        for (int i = 0; i < 5; i++) {
            long startTime = System.nanoTime();
            ExecutorService service = Executors.newFixedThreadPool(threadNum);
            service.execute(() -> {
                for (int k = 0; k < MAX_DATA; k++) {
                    Integer temp = (int) Math.ceil(Math.random() * 600000);
                    if (hasWrite) map.get(String.valueOf(temp));
                    map.put(String.valueOf(temp), temp);
                }
            });
            service.shutdown();
            service.awaitTermination(2, TimeUnit.HOURS);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000L;
            System.out.println(threadNum * MAX_DATA + " elements inserted/retrieved in " + duration + " ms");
            totalTime += duration;
        }

        System.out.println("For the map [" + map.getClass() + "] average time is: " + (totalTime / 5) + " ms");
    }

}
