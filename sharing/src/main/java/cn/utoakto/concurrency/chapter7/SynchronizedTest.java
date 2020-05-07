package cn.utoakto.concurrency.chapter7;

/**
 * synchronized 关键字
 *
 * @date 2019/4/8
 */
public class SynchronizedTest {

    private final static Object LOCK = new Object();

    public static void main(String[] args) {

        /**
         * 打开jconsole和jstack可以看到详细信息
         */
        Runnable runnable = () -> {
            synchronized (LOCK) {
                try {
                    Thread.sleep(900_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);

        t1.start();
        t2.start();
        t3.start();

    }

}
