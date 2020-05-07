package cn.utokato.juc.atomic;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * {@link AtomicBoolean} 使用一个 volatile int value 来实现
 * 0 代表 false
 * 1 代表 true
 *
 * @date 2019/5/16
 */
public class AtomicBooleanTest {


    @Test
    public void testCreate() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        assertFalse(atomicBoolean.get());
        AtomicBoolean atomicBoolean1 = new AtomicBoolean(true);
        assertTrue(atomicBoolean1.get());
    }

    @Test
    public void testGetAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        // result 是 previous value
        boolean result = atomicBoolean.getAndSet(false);
        assertTrue(result);
        assertFalse(atomicBoolean.get());
    }

    @Test
    public void testCompareAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        // result 表示更改是否成功，true代表真实值与期望值相同，修改成功；false表示真实值与期望值不同，修改失败
        boolean result = atomicBoolean.compareAndSet(true, false);
        assertTrue(result);
        assertFalse(atomicBoolean.get());
    }

    @Test
    public void testCompareAndSetFailed() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        // result 表示更改是否成功，true代表真实值与期望值相同，修改成功；false表示真实值与期望值不同，修改失败
        boolean result = atomicBoolean.compareAndSet(false, true);
        assertFalse(result);
        assertTrue(atomicBoolean.get());
    }


}
