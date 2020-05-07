package cn.utoakto.concurrency.chapter2;

/**
 * 使用{@link Runnable}接口的方式
 * 该接口对需要统一操作的数据进行了集中的定义
 *
 *
 * @date 2019/4/7
 */
public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    /**
     * 这里不需要static来修改该变量
     */
    private final int MAX = 50;


    @Override
    public void run() {
        while (index <= MAX) {
            try {
                Thread.sleep(500 * 1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " -> 当前票的号码是：" + (index++));
        }
    }
}
