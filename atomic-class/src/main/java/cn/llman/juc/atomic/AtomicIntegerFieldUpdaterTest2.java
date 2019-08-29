package cn.llman.juc.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @date 2019/5/16
 */
public class AtomicIntegerFieldUpdaterTest2 {

    private volatile int i;

    private AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterTest2> updater
            = AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterTest2.class, "i");

    public void update(int newValue) {
        updater.compareAndSet(this, i, newValue);
    }

    public int get() {
        return i;
    }

    public static void main(String[] args) {
        AtomicIntegerFieldUpdaterTest2 test = new AtomicIntegerFieldUpdaterTest2();
        test.update(10);
        System.out.println(test.get());
    }
}
