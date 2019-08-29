package cn.llman.juc.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static org.junit.Assert.*;

/**
 * {@link AtomicIntegerArray}
 * {@link AtomicLongArray}
 * {@link AtomicReferenceArray}
 *
 *
 * @date 2019/5/16
 */
public class AtomicIntegerArrayTest {


    @Test
    public void testCreate() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        assertEquals(10, atomicIntegerArray.length());
    }

    @Test
    public void testGet() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        assertEquals(0, atomicIntegerArray.get(5));
    }

    @Test
    public void testSet() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        atomicIntegerArray.set(5, 10);
        assertEquals(10, atomicIntegerArray.get(5));
    }

    @Test
    public void testGetAndSet() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        int oldValue = atomicIntegerArray.getAndSet(5, 15);
        assertEquals(0, oldValue);
        assertEquals(15, atomicIntegerArray.get(5));
    }

    @Test
    public void testCreateWithArray() {
        int[] originArray = {1, 3, 5, 7, 9};
        // 在构造函数中传入一个数组，AtomicIntegerArray 内部会将该数组进行拷贝
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(originArray);
        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(atomicIntegerArray.get(i));
        }
    }


}
