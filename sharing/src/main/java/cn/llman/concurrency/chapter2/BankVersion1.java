package cn.llman.concurrency.chapter2;

/**
 * 场景：模拟一个银行的叫号服务，多个窗口同时操作一个index数据
 *
 *
 * @date 2019/4/7
 */
public class BankVersion1 {

    public static void main(String[] args) {
        TicketWindow t1 = new TicketWindow("1号窗口");
        t1.start();

        TicketWindow t2 = new TicketWindow("2号窗口");
        t2.start();

        TicketWindow t3 = new TicketWindow("3号窗口");
        t3.start();
    }
}
