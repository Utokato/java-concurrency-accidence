package cn.utokato.juc.utils.countDownLatch;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @date 2019/5/19
 */
public class CountDownLatchExample1 {

    private final static Random random = new Random(System.currentTimeMillis());

    private final static ExecutorService executor = Executors.newFixedThreadPool(10);

    private final static CountDownLatch latch = new CountDownLatch(10);


    public static void main(String[] args) throws InterruptedException {
        // (1) 模拟从数据库中获取10条数据
        int[] data = query();
        // (2) 在线程池内，同时处理这10条数据
        for (int i = 0; i < data.length; i++) {
            executor.execute(new SimpleRunnable(data, i, latch));
        }
        // (3) 当所有数据处理完毕后，再进行其他的操作(保存、发送等)
        latch.await();
        System.out.println("All of work were done!");
        executor.shutdown();

    }

    static class SimpleRunnable implements Runnable {

        private final int[] data;
        private final int index;
        private final CountDownLatch latch;

        SimpleRunnable(int[] data, int index, CountDownLatch latch) {
            this.data = data;
            this.index = index;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int value = data[index];
            if (value % 2 == 0) {
                data[index] = value * 2;
            } else {
                data[index] = value * 10;
            }
            System.out.println(Thread.currentThread().getName() + " finished!");
            latch.countDown();
        }
    }

    private static int[] query() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }


}
