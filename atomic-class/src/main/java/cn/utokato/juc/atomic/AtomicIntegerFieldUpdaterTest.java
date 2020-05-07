package cn.utokato.juc.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @date 2019/5/16
 */
public class AtomicIntegerFieldUpdaterTest {

    public static void main(String[] args) {
        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i");
        TestMe me = new TestMe();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                final int MAX = 20;
                for (int j = 0; j < MAX; j++) {
                    int v = updater.getAndIncrement(me);
                    System.out.println(Thread.currentThread().getName() + " => " + v);
                }
            }
            ).start();
        }
    }

    static class TestMe {
        volatile int i;
    }

    /**
     * Can't access the private field of object
     */
    @Test(expected = RuntimeException.class)
    public void testPrivateFieldAccessError() {
        AtomicIntegerFieldUpdater<cn.utokato.juc.atomic.TestMe> updater
                = AtomicIntegerFieldUpdater.newUpdater(cn.utokato.juc.atomic.TestMe.class, "i");
        cn.utokato.juc.atomic.TestMe me = new cn.utokato.juc.atomic.TestMe();
        boolean success = updater.compareAndSet(me, 0, 1);
        System.out.println(success);
    }

    @Test(expected = ClassCastException.class)
    public void testTargetObjectIsNull() {
        AtomicIntegerFieldUpdater<cn.utokato.juc.atomic.TestMe> updater
                = AtomicIntegerFieldUpdater.newUpdater(cn.utokato.juc.atomic.TestMe.class, "i");
        updater.compareAndSet(null, 0, 1);
    }


    /**
     * NoSuchFieldException
     */
    @Test(expected = RuntimeException.class)
    public void testFiledNameInvalid() {
        AtomicIntegerFieldUpdater<cn.utokato.juc.atomic.TestMe> updater
                = AtomicIntegerFieldUpdater.newUpdater(cn.utokato.juc.atomic.TestMe.class, "i1");
        updater.compareAndSet(null, 0, 1);
    }

    /**
     *
     */
    @Test
    public void testFiledTypeInvalid() {
        AtomicReferenceFieldUpdater<TestMe2, Integer> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Integer.class, "i");
        TestMe2 me2 = new TestMe2();
        updater.compareAndSet(me2, null, 1);
    }

    /**
     * java.lang.IllegalArgumentException: Must be volatile type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFiledIsNotVolatile() {
        AtomicReferenceFieldUpdater<TestMe2, Integer> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Integer.class, "i");
        TestMe2 me2 = new TestMe2();
        updater.compareAndSet(me2, null, 1);
    }


    static class TestMe2 {
        /*volatile*/ Integer i;
    }

}
