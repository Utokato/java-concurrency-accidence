package cn.utokato.juc.collections.blocking;

import org.junit.Test;

import java.util.concurrent.SynchronousQueue;

/**
 * {@link SynchronousQueue}
 * 一种 transfer 机制
 *
 * @author lma
 * @date 2019/06/07
 */
public class SynchronousQueueExample {

    public <T> SynchronousQueue<T> create() {
        return new SynchronousQueue<>();
    }

    /**
     * -- add 和 offer 向队列中加入数据时，必须要另外一个线程从数据中获取数据，不然就会抛出异常
     * {@link SynchronousQueue#add(Object)}
     * {@link SynchronousQueue#offer(Object)}
     * <p>
     * -- put 向队列中添加数据后，会陷入阻塞，直到另外的线程从队列中消费了数据
     * {@link SynchronousQueue#put(Object)}
     */
    @Test
    public void testInsert() {
        SynchronousQueue<String> synchronousQueue = create();
    }


    /**
     * {@link SynchronousQueue#peek()}  Always returns {@code null}.
     * {@link SynchronousQueue#poll()}
     * {@link SynchronousQueue#remove()}
     * {@link SynchronousQueue#take()} 会陷入阻塞
     */
    @Test
    public void testGetData() {

    }
}
