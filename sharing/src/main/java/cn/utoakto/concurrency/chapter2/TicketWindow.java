package cn.utoakto.concurrency.chapter2;

/**
 * @date 2019/4/7
 */
public class TicketWindow extends Thread {

    private final String windowName;


    /**
     * 注意这里static的使用，以及static的弊端
     * 如果不加static修饰这两个变量，每个TicketWindow实例都会有一个各自的MAX和index
     * 不能满足多个线程同时操作同一份数据的需要
     * <p>
     * 加了static关键字修饰这两个变量后，在运行中JVM对这两个变量只会初始化异常，并且这两个变量有它们自己额外的空间
     * 而且，会随着整个线程的生命周期而存在，这就是static的弊端
     */
    private static final int MAX = 50;
    private static int index = 1;

    public TicketWindow(String windowName) {
        this.windowName = windowName;
    }

    @Override
    public void run() {

        while (index <= MAX) {
            try {
                Thread.sleep(1000 * 1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("银行窗口：" + windowName + " -> 当前票的号码是：" + (index++));
        }

    }
}
