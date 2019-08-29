package cn.llman.juc.collections.blocking;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * {@link LinkedBlockingQueue} 边界可选
 *
 *
 * @author lma
 * @date 2019/06/07
 */
public class LinkedBlockingQueueExample {

    public <T> LinkedBlockingQueue<T> create(int size) {
        return new LinkedBlockingQueue<>(size);
    }

    public <T> LinkedBlockingQueue<T> create() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * {@link LinkedBlockingQueue#add(Object)}
     * {@link LinkedBlockingQueue#offer(Object)}
     * {@link LinkedBlockingQueue#put(Object)}
     *
     * 操作链表是通过私有方法 enqueue 和 dequeue 来实现的
     */
    @Test
    public void testInsertData() {
        LinkedBlockingQueue<String> queue = create();
    }

    /**
     * {@link LinkedBlockingQueue#peek()}
     * {@link LinkedBlockingQueue#element()}
     * {@link LinkedBlockingQueue#remove()}
     * {@link LinkedBlockingQueue#take()}
     *
     */
    @Test
    public void testGetData(){

    }
}
