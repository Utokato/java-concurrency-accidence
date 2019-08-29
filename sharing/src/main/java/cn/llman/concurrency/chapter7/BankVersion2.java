package cn.llman.concurrency.chapter7;

/**
 * @date 2019/4/8
 */
public class BankVersion2 {

    public static void main(String[] args) {
        final TicketWindowRunnable ticketWindowRunnable = new TicketWindowRunnable();

        Thread t1 = new Thread(ticketWindowRunnable, "一号窗口");
        Thread t2 = new Thread(ticketWindowRunnable, "二号窗口");
        Thread t3 = new Thread(ticketWindowRunnable, "三号窗口");

        t1.start();
        t2.start();
        t3.start();
    }
}
