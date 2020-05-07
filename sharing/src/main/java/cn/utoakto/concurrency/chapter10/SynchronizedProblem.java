package cn.utoakto.concurrency.chapter10;

/**
 * @date 2019/4/9
 */
public class SynchronizedProblem {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            SynchronizedProblem.run();
        }, "T1");
        t1.start();


        Thread t2 = new Thread(() -> {
            SynchronizedProblem.run();
        }, "T2");
        t2.start();

        Thread.sleep(2_000);
        /**
         * 线程t1进入了同步方法内进行执行
         * 线程t2就blocked，然后通过interrupt方法去中断线程t2，线程而还是会处于blocked状态
         * 这是由于synchronized方法或代码块带来的小问题
         *
         * 值得再次注意的是：interrupt只能打断{@link Thread#join()}、{@link Thread#sleep(long)}和{@link Object#wait()}方法进入的block状态
         *
         * synchronized方法或代码块带来的blocked状态，不能通过interrupt进行打断
         */
        t2.interrupt();
        System.out.println(t2.isInterrupted());


    }

    private synchronized static void run() {
        System.out.println(Thread.currentThread().getName());
        while (true) {

        }
    }
}
