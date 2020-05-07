package cn.utokato.juc.collections.blocking;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * {@link LinkedBlockingDeque}
 * Double-End-Queue
 *
 * @author lma
 * @date 2019/06/07
 */
public class LinkedBlockingDequeExample {

    public <T> LinkedBlockingDeque<T> create() {
        return new LinkedBlockingDeque<>();
    }

    @Test
    public void testAddFirst() {
        LinkedBlockingDeque<String> deque = create();

        deque.addFirst("Hello");
        deque.addFirst("World");

        assertThat(deque.removeFirst(), equalTo("World"));
        assertThat(deque.removeFirst(), equalTo("Hello"));

    }

    @Test
    public void testAdd() {
        LinkedBlockingDeque<String> deque = create();

        deque.add("Hello");
        deque.add("World");

        assertThat(deque.removeFirst(), equalTo("Hello"));
        assertThat(deque.removeFirst(), equalTo("World"));

    }

    @Test
    public void testTakeFirst() throws InterruptedException {
        LinkedBlockingDeque<String> deque = create();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> deque.addFirst("Hello"), 1, TimeUnit.SECONDS);
        service.shutdown();

        long currentTime = System.currentTimeMillis();
        String result = deque.takeFirst();
        assertThat(System.currentTimeMillis() - currentTime >= 1000, equalTo(true));
        System.out.println(result);

    }

}
