package cn.llman.juc.collections.blocking;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * {@link ArrayBlockingQueue} 固定边界
 *
 * @author lma
 * @date 2019/06/06
 */
public class ArrayBlockingQueueExample {

    private <E> ArrayBlockingQueue<E> create(int size) {
        return new ArrayBlockingQueue<>(size);
    }

    private ArrayBlockingQueue<String> queue;

    @Before
    public void init() {
        queue = create(5);
    }

    /**
     * add 向队列中插入元素
     * 当队列full时，再次插入元素，会抛出异常
     */
    @Test
    public void testAddNotExceedCapacity() {
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.size(), equalTo(5));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddExceedCapacity() {
        assertThat(queue.add("Healo1"), equalTo(true));
        assertThat(queue.add("Healo2"), equalTo(true));
        assertThat(queue.add("Healo3"), equalTo(true));
        assertThat(queue.add("Healo4"), equalTo(true));
        assertThat(queue.add("Healo5"), equalTo(true));
        assertThat(queue.add("Healo6"), equalTo(true));
        fail("Should not process here");
    }

    /**
     * offer 向队列中插入元素
     * 当队列full时，再次插入元素，会返回false
     */
    @Test
    public void testOfferNotExceedCapacity() {
        assertThat(queue.offer("Hello1"), equalTo(true));
        assertThat(queue.offer("Hello2"), equalTo(true));
        assertThat(queue.offer("Hello3"), equalTo(true));
        assertThat(queue.offer("Hello4"), equalTo(true));
        assertThat(queue.offer("Hello5"), equalTo(true));
        assertThat(queue.size(), equalTo(5));
    }

    @Test
    public void testOfferExceedCapacity() {
        assertThat(queue.offer("Heloo1"), equalTo(true));
        assertThat(queue.offer("Heloo2"), equalTo(true));
        assertThat(queue.offer("Heloo3"), equalTo(true));
        assertThat(queue.offer("Heloo4"), equalTo(true));
        assertThat(queue.offer("Heloo5"), equalTo(true));
        assertThat(queue.offer("Heloo5"), equalTo(false));
        assertThat(queue.size(), equalTo(5));
    }

    /**
     * put 向队列中插入元素
     * 当队列full时，再次插入元素，会陷入到阻塞中
     * 当有其他线程从队列中拿走元素时，put会从阻塞中break，然后将元素放入队列中
     */
    @Test
    public void testPutNotExceedCapacity() throws InterruptedException {
        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        assertThat(queue.size(), equalTo(5));
    }

    @Test
    public void testPutExceedCapacity() throws InterruptedException {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            try {
                assertThat(queue.take(), equalTo("Hello1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);

        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        queue.put("Hello6");
        service.shutdown();
    }

    /**
     * poll 从队首取出一个元素
     * 队列为空时，返回null
     */
    @Test
    public void testPoll() {
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        queue.add("Hello4");
        queue.add("Hello5");

        /////////////////////

        assertThat(queue.poll(), equalTo("Hello1"));
        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.poll(), equalTo("Hello3"));
        assertThat(queue.poll(), equalTo("Hello4"));
        assertThat(queue.poll(), equalTo("Hello5"));
        assertThat(queue.poll(), nullValue());

    }

    /**
     * 查看队首的元素
     * 队列为空时，返回null
     */
    @Test
    public void testPeek() {
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        queue.add("Hello4");
        queue.add("Hello5");

        /////////////////////

        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
    }

    /**
     * 查看队首的元素
     * 队列为空时，抛出一个异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement() {
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        queue.add("Hello4");
        queue.add("Hello5");

        /////////////////////

        assertThat(queue.element(), equalTo("Hello1"));
        assertThat(queue.element(), equalTo("Hello1"));
        queue.clear();
        assertThat(queue.element(), equalTo("Hello1"));
    }

    /**
     * 从队首移除一个元素
     * 队列为空时，抛出一个异常
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        queue.add("Hello4");
        queue.add("Hello5");

        /////////////////////

        assertThat(queue.remove(), equalTo("Hello1"));
        assertThat(queue.remove(), equalTo("Hello2"));
        assertThat(queue.remove(), equalTo("Hello3"));
        assertThat(queue.remove(), equalTo("Hello4"));
        assertThat(queue.remove(), equalTo("Hello5"));
        assertThat(queue.remove(), equalTo("Hello1"));
    }

    @Test
    public void testDrainTo() {
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        assertThat(queue.size(), equalTo(3));
        assertThat(queue.remainingCapacity(), equalTo(2));

        ArrayList<String> list = new ArrayList<>();
        queue.drainTo(list);

        assertThat(list.size(), equalTo(3));
    }
}
