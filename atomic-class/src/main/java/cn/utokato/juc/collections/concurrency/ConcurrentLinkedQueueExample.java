package cn.utokato.juc.collections.concurrency;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author lma
 * @date 2019/06/11
 */
public class ConcurrentLinkedQueueExample {
    public static void main(String[] args) {
        final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 100000; i++) {
            queue.offer(System.nanoTime());
        }

        System.out.println("=== insert done ===");

        /**
         * 踩坑注意
         * 判断queue中是否还有元素
         * 1. {@link ConcurrentLinkedQueue#size()} 是否大于 0
         * 2. {@link ConcurrentLinkedQueue#isEmpty()} 是否为空
         *
         */
        long startTime = System.currentTimeMillis();
        while (queue.size() > 0) {
            System.out.println(queue.poll());
        }
        /*while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }*/
        System.out.println(System.currentTimeMillis() - startTime);


    }


    private static void handleText(String s) {
        if (s != null && !"".equals(s)) {

        }

        // --

        if (s != null && s.length() > 0) {

        }
        if (s != null && !s.isEmpty()) {

        }
    }
}
