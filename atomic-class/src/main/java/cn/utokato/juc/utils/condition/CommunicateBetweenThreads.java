package cn.utokato.juc.utils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 使用{@link Object#wait()} 和 {@link Object#notify()} 进行线程间的通讯
 *
 * @date 2019/5/23
 */
public class CommunicateBetweenThreads {

    private static int data = 0;

    private static boolean noUse = true;

    private final static Object monitor = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            for (; ; ) {
                buildData();
            }
        }).start();

        new Thread(() -> {
            for (; ; ) {
                useData();
            }
        }).start();
    }

    private static void buildData() {
        synchronized (monitor) {
            while (noUse) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data++;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Optional.of("P: " + data).ifPresent(System.out::println);
            noUse = true;
            monitor.notify();
        }
    }

    private static void useData() {
        synchronized (monitor) {
            while (!noUse) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Optional.of("C: " + data).ifPresent(System.out::println);
            noUse = false;
            monitor.notify();
        }
    }
}
