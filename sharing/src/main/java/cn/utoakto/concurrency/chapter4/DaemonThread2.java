package cn.utoakto.concurrency.chapter4;

/**
 * @date 2019/4/8
 */
public class DaemonThread2 {

    public static void main(String[] args) {

        Thread t = new Thread(() -> {
            Thread inner = new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("do something for health check");
                        Thread.sleep(1_000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            inner.setDaemon(true); // 这个线程设置为守护线程后，它会随着它的外部线程t的终止而终止
            inner.start();

            try {
                Thread.sleep(1_000);
                System.out.println("T thread finish done!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        // t.setDaemon(true);// 将该线程设置为守护线程，必须要在该线程启动前设置才能生效

        t.start();
    }
}
