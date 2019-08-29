package cn.llman.juc.collections.blocking;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * {@link LinkedTransferQueue}
 * 在之前的队列中，producer将数据放入队列中，就直接返回了，
 * 并不知道该数据的消费情况；
 * <p>
 * {@link LinkedTransferQueue} 会等待其中的数据进行消费
 * 只有该数据有一个consumer线程接受了，才表明该数据进行了正确地入队
 *
 * @author lma
 * @date 2019/06/08
 */
public class LinkedTransferQueueExample {

    public <T> LinkedTransferQueue<T> create() {
        return new LinkedTransferQueue<>();
    }

    /**
     * {@link LinkedTransferQueue#tryTransfer(Object)}
     * 返回Boolean
     * true：数据进行了正常的转换，入队成功
     * false：没有consumer接手该数据，入队失败。入队失败后，队列的size不变
     */
    @Test
    public void testTryTransfer() {
        LinkedTransferQueue<String> transferQueue = create();
        boolean result = transferQueue.tryTransfer("transfer");
        assertThat(result, equalTo(false));
        assertThat(transferQueue.size(), equalTo(0));
    }

    @Test
    public void testTransfer() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = create();


        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            try {
                String result = transferQueue.poll(1, TimeUnit.SECONDS);
                assertThat(result, equalTo("transfer"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
        service.shutdown();


        long currentTime = System.currentTimeMillis();
        transferQueue.transfer("transfer");
        assertThat(transferQueue.size(), equalTo(0));
        System.out.println(System.currentTimeMillis() - currentTime);
    }

    @Test
    public void testTransfer2() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = create();
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(0));
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(false));


        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        IntStream.range(0, 5).boxed().map(i -> (Callable<String>) () -> {
            try {
                return transferQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).forEach(cachedThreadPool::submit);


        TimeUnit.MILLISECONDS.sleep(100); // 确保线程池中的线程都能启动

        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(5));
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(true));
        System.out.println("Deal to this point");

        IntStream.range(0, 5).boxed().map(String::valueOf).forEach(transferQueue::add);

        TimeUnit.MILLISECONDS.sleep(100);
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(0));
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(false));

    }

    @Test(expected = NullPointerException.class)
    public void testAdd() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = create();
        boolean result = transferQueue.add("Hello");

        assertThat(transferQueue.size(), equalTo(1));
        assertThat(result, equalTo(true));

        transferQueue.add(null);
    }
}
