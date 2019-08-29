package cn.llman.concurrency.chapter6;

import org.junit.Test;

/**
 * 如何强制关闭一个线程
 * {@link ThreadService}
 *
 * @date 2019/4/8
 */
public class ThreadCloseForce {

    /**
     * 线程任务加载超时
     * 强制中断
     */
    @Test
    public void testOvertimeTask() {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();

        service.execute(() -> {
            System.out.println("Entering heavy mission and processing task ...");
            // mock to load a very heavy source.
            while (true) {

            }
        });

        service.shutdown(10_000L);
        long end = System.currentTimeMillis();

        System.out.println("耗时：" + (end - start));
    }

    /**
     * 线程任务正常执行
     */
    @Test
    public void testNormalTask() {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();

        service.execute(() -> {
            System.out.println("Entering a normal mission and processing task ...");
            try {
                Thread.sleep(5_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown(10_000L);
        long end = System.currentTimeMillis();

        System.out.println("耗时：" + (end - start));
    }
}
