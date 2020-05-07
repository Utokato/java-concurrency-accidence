package cn.utokato.juc.collections.blocking;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * {@link DelayQueue} 延迟队列
 * <p>
 * 1. 延迟队列中的元素，必须实现{@link Delayed}接口；
 * 2. 所有的元素按照过期时间的长短来排序，过期时间越短越靠近队首 （通过{@link Comparable}中的方法来定义）
 * 3. 如果队列中所有的元素都没有过去，take方法会等待，直到有一个元素过期，并将其返回
 * -    poll/remove 不会等待，直接返回一个null
 * -    peek 方法会返回队首的元素，不论这个元素过期与否
 * 4. 队列中不允许加入null
 *
 * @author lma
 * @date 2019/06/07
 */
public class DelayQueueExample {

    public <T extends Delayed> DelayQueue<T> create() {
        return new DelayQueue<>();
    }

    /**
     * {@link DelayQueue#peek()} 返回队首的元素，但不会移除该元素
     * 不论队首的元素是否过期，都会返回队首的元素
     * <p>
     * 当队列为空时，返回null
     */
    @Test
    public void test1() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = create();
        DelayElement<String> delayElement = DelayElement.of("Delay1", 1000);
        delayQueue.add(delayElement);
        assertThat(delayQueue.size(), equalTo(1));
        long startTime = System.currentTimeMillis();
        // assertThat(delayQueue.peek(), equalTo(delayElement));
        assertThat(delayQueue.take(), equalTo(delayElement));
        System.out.println(System.currentTimeMillis() - startTime);
    }

    /**
     * {@link DelayQueue#iterator()} 迭代器可以快速查看队列中所有的元素
     * 但不会消费其中的数据
     */
    @Test
    public void test2() {
        DelayQueue<DelayElement<String>> delayQueue = create();
        delayQueue.add(DelayElement.of("Delay1", 1000));
        delayQueue.add(DelayElement.of("Delay2", 900));
        delayQueue.add(DelayElement.of("Delay3", 800));
        delayQueue.add(DelayElement.of("Delay4", 700));

        assertThat(delayQueue.size(), equalTo(4));

        long startTime = System.currentTimeMillis();
        Iterator<DelayElement<String>> it = delayQueue.iterator();
        while (it.hasNext()) {
            assertThat(it.next(), notNullValue());
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println(duration);
        assertThat(duration < 5, equalTo(true));

    }

    /**
     * {@link DelayQueue} 队列中的元素按照过期时间长短进行排序
     * 过期时间越短，越靠近队首
     */
    @Test
    public void test3() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = create();
        delayQueue.add(DelayElement.of("Delay1", 900));
        delayQueue.add(DelayElement.of("Delay2", 800));
        delayQueue.add(DelayElement.of("Delay3", 700));
        delayQueue.add(DelayElement.of("Delay4", 600));

        assertThat(delayQueue.size(), equalTo(4));

        assertThat(delayQueue.take().getE(), equalTo("Delay4"));

    }

    @Test(expected = NoSuchElementException.class)
    public void test4() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = create();
        delayQueue.add(DelayElement.of("Delay1", 901));
        delayQueue.add(DelayElement.of("Delay2", 801));
        delayQueue.add(DelayElement.of("Delay3", 701));
        delayQueue.add(DelayElement.of("Delay4", 601));

        assertThat(delayQueue.size(), equalTo(4));

        assertThat(delayQueue.remove().getE(), equalTo("Delay4"));

    }

    @Test
    public void test5() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = create();
        delayQueue.add(DelayElement.of("Delay1", 902));
        delayQueue.add(DelayElement.of("Delay2", 802));
        delayQueue.add(DelayElement.of("Delay3", 702));
        delayQueue.add(DelayElement.of("Delay4", 60));

        assertThat(delayQueue.size(), equalTo(4));

        TimeUnit.MILLISECONDS.sleep(80);
        DelayElement<String> delayElement = delayQueue.poll();
        System.out.println(delayElement);
    }

    @Test
    public void test6() {
        DelayQueue<DelayElement<String>> delayQueue = create();
        try {
            delayQueue.add(null);
            fail("Should not process here");
        } catch (Exception e) {
            assertThat(e instanceof NullPointerException, equalTo(true));
        }
    }

    /**
     * {@link DelayQueue#poll()} 队列为空时，poll返回null
     */
    @Test
    public void test7() {
        DelayQueue<DelayElement<String>> delayQueue = create();
        assertThat(delayQueue.poll(), nullValue());
    }

    /**
     * {@link DelayQueue#take()} 队列为空时，take陷入阻塞等待状态
     */
    @Test
    public void test8() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = create();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            delayQueue.add(DelayElement.of("take", 1000));
        }, 1, TimeUnit.SECONDS);
        service.shutdown();

        long startTime = System.currentTimeMillis();
        assertThat(delayQueue.take().getE(), equalTo("take"));
        System.out.println(System.currentTimeMillis() - startTime);
    }


    static class DelayElement<E> implements Delayed {

        private final E e;
        private final long expireTime;

        DelayElement(E e, long delay) {
            this.e = e;
            this.expireTime = System.currentTimeMillis() + delay;
        }

        static <T> DelayElement<T> of(T t, long delay) {
            return new DelayElement<>(t, delay);
        }

        /**
         * 预计过期时间 与 当前系统时间
         * <p>
         * 预计过期时间 > 当前系统时间 -> 没有过期 不能获取数据
         * 预计过期时间 < 当前系统时间 -> 已经过期 可以获取数据
         * <p>
         * 即：预计过期时间 - 当前系统时间 > 0 不能获取数据
         * -   预计过期时间 - 当前系统时间 <= 0 可以获取数据
         */
        @Override
        public long getDelay(TimeUnit unit) {

            long diff = expireTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            DelayElement otherElement = (DelayElement) other;
            return Long.compare(this.getExpireTime(), otherElement.getExpireTime());
        }

        public E getE() {
            return e;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }

}
