package cn.llman.concurrency.chapter7;

/**
 * Synchronized 同步方法
 *
 * @date 2019/4/8
 */
public class TicketWindowSynchronizedRunnable implements Runnable {
    private int index = 1;

    // read only shared data
    private final int MAX = 500;


    @Override
    public void run() {
        while (true) {
            if (ticket()) break;
        }

    }

    // 此时synchronized锁定的是对象是 this
    // 同步方法
    private synchronized boolean ticket() {
        // 1. getField;
        if (index > MAX) return true;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // index++ ==> index = index + 1;
        // 1. get field index
        // 2. index = index + 1;
        // 3. put field index
        System.out.println(Thread.currentThread().getName() + " -> 当前票的号码是：" + (index++));
        return false;

    }
}
