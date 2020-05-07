package cn.utoakto.concurrency.chapter4;

/**
 * @date 2019/4/8
 */
public class DaemonThread {

    public static void main(String[] args) {

        // new
        Thread t = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " running...");
                Thread.sleep(10_000);// JDK1.7后数字千分制的新写法
                System.out.println(Thread.currentThread().getName() + " done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setDaemon(true); // 如果把Thread-0设置为守护线程，则main线程结束了，Thread-0也就随之terminated

        // running -> runnable|blocked|terminated
        t.start();

        System.out.println(Thread.currentThread().getName());


        /**
         * main                     main线程执行完就直接terminated了，但整个应用由于还有其他的非守护线程存在，所以不会里面退出
         * Thread-0 running...      Thread-0 开始运行后，经过sleep()后进入blocked状态
         * Thread-0 done!
         */
    }
}

/**
 * A<-------------------------->B
 * 假如A和B之间建立了长连接，长连接间需要通过心跳机制来保证连接
 * 一般心跳机制和业务无关，所以可以使用一个独立的线程来做
 * 为了保证业务线程与心跳线程的同步，可以将心跳线程设置为daemonThread
 * 当业务线程结束时，心跳线程也随之结束
 */
