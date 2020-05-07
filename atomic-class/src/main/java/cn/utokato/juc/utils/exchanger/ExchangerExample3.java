package cn.utokato.juc.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @date 2019/5/20
 */
public class ExchangerExample3 {
    /**
     * Exchanger 在两个线程之间，是可以重复多次使用的
     * <p>
     * 这也就以为着，两个线程之间可以一直通过一个{@link Exchanger}来共享彼此当前的数据
     */
    public static void main(String[] args) {

        final Exchanger<Integer> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                AtomicReference<Integer> value = new AtomicReference<>(1);
                while (true) {
                    value.set(exchanger.exchange(value.get()));
                    System.out.println("Thread A has value: " + value);
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "A").start();

        new Thread(() -> {
            try {
                AtomicReference<Integer> value = new AtomicReference<>(2);
                while (true) {
                    value.set(exchanger.exchange(value.get()));
                    System.out.println("Thread B has value: " + value);
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "B").start();

    }
}
