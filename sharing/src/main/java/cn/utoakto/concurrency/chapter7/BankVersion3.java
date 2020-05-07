package cn.utoakto.concurrency.chapter7;

/**
 * @date 2019/4/8
 */
public class BankVersion3 {

    public static void main(String[] args) {
        final TicketWindowSynchronizedRunnable ticketWindow = new TicketWindowSynchronizedRunnable();

        Thread t1 = new Thread(ticketWindow, "一号窗口");
        Thread t2 = new Thread(ticketWindow, "二号窗口");
        Thread t3 = new Thread(ticketWindow, "三号窗口");

        t1.start();
        t2.start();
        t3.start();
    }
}
