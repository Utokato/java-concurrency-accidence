package cn.llman.juc.collections.concurrency;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.*;
        import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ConcurrentSkipListMap} VS {@link ConcurrentHashMap}
 *
 * @author lma
 * @date 2019/06/11
 */
public class ConcurrentSkipListMapVSConcurrentHashMap {

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

    private final static Map<Class<?>, List<Entry>> summary = new HashMap<Class<?>, List<Entry>>() {
        {
            put(ConcurrentHashMap.class, new ArrayList<>());
            put(ConcurrentSkipListMap.class, new ArrayList<>());
        }
    };

    public static void main(String[] args) throws InterruptedException {
        for (int i = 10; i <= 100; i += 10) {
            pressureTest(new ConcurrentHashMap<>(), i, true);
            pressureTest(new ConcurrentSkipListMap<>(), i, true);
        }

        summary.forEach((k, v) -> {
            System.out.println("---start---");
            System.out.println(k.getSimpleName());
            v.forEach(System.out::println);
            System.out.println("---end---");
        });
    }

    private static void pressureTest(final Map<String, Integer> map, int threadNum, boolean hasWrite) throws InterruptedException {
        System.out.println("Start pressure testing for map [" + map.getClass() + "] use " + threadNum + " threads, and write is " + hasWrite);
        long totalTime = 0L;
        final int MAX_DATA = 50_0000;
        for (int i = 0; i < 5; i++) {
            final AtomicInteger counter = new AtomicInteger(0);
            map.clear();
            long startTime = System.nanoTime();
            ExecutorService service = Executors.newFixedThreadPool(threadNum);

            service.execute(() -> {
                for (int k = 0; k < MAX_DATA && counter.getAndIncrement() < MAX_DATA; k++) {
                    Integer temp = (int) Math.ceil(Math.random() * 600000);
                    if (hasWrite) map.get(String.valueOf(temp));
                    map.put(String.valueOf(temp), temp);
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

        long avgDuration = totalTime / 5;
        summary.get(map.getClass()).add(new Entry(threadNum, avgDuration));
        System.out.println("For the map [" + map.getClass() + "] average time is: " + avgDuration + " ms");
    }


}
