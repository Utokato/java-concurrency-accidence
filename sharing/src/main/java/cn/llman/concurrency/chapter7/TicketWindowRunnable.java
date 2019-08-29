package cn.llman.concurrency.chapter7;

/**
 * @date 2019/4/8
 */
public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    /**
     * 这里不需要static来修改该变量
     * <p>
     * 共享数据
     */
    private final int MAX = 500;

    private final Object MONITOR = new Object();


    @Override
    public void run() {
        // 1.
        synchronized (MONITOR) {
            while (true) {
                double d = Math.random();
                int i = (int) (d * 500);
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (index > MAX) break;
                System.out.println(Thread.currentThread().getName() + " -> 当前票的号码是：" + (index++) + ", 处理业务销毁时间为：" + i + "s.");
            }
        }
        // 2.
        // 在1-2的位置间，也就是synchronized内部，是单线程执行的
    }
}
