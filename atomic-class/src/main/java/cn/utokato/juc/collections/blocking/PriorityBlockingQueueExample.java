package cn.utokato.juc.collections.blocking;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * {@link PriorityBlockingQueue} 无边界，边界为整型的最大值
 *
 * @author lma
 * @date 2019/06/06
 */
public class PriorityBlockingQueueExample {

    public <T> PriorityBlockingQueue<T> create(int size) {
        return new PriorityBlockingQueue<>(size);
    }

    public <T> PriorityBlockingQueue create(int size, Comparator comparator) {
        return new PriorityBlockingQueue<>(size, comparator);
    }

    private PriorityBlockingQueue<String> queue;

    @Before
    public void init() {
        queue = new PriorityBlockingQueue<>();
    }

    /**
     * {@link PriorityBlockingQueue#add(Object)}
     * {@link PriorityBlockingQueue#offer(Object)} }
     * {@link PriorityBlockingQueue#put(Object)} }
     */
    @Test
    public void testAdd() {
    }

    /**
     * -- 查看队首的元素
     * {@link PriorityBlockingQueue#element()}
     * {@link PriorityBlockingQueue#peek()}
     * -- 弹出队首的元素 包含了一个 dequeue 的过程
     * {@link PriorityBlockingQueue#remove()}
     * {@link PriorityBlockingQueue#poll()}
     * -- dequeue，如果队首没有元素就陷入阻塞
     * -- 当其他线程向该队列中加入一个数据时，take会从阻塞中出来，并取出当前队首的元素
     * {@link PriorityBlockingQueue#take()} //
     */
    @Test
    public void testGet() {

    }

    @Test(expected = ClassCastException.class)
    public void testAdd_WithoutComparable_WithoutComparator() {
        PriorityBlockingQueue<Object> blockingQueue = create(2);
        blockingQueue.add(new UserWithoutComparable());
        fail("Should not process here.");
    }

    @Test
    public void testAdd_WithoutComparable_WithComparator() {
        PriorityBlockingQueue blockingQueue = create(2, Comparator.comparingInt(Object::hashCode));
        blockingQueue.add(new UserWithoutComparable());
        assertThat(blockingQueue.size(), equalTo(1));
    }

    static class UserWithoutComparable {

    }

}
