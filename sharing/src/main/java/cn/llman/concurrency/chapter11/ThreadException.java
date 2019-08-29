package cn.llman.concurrency.chapter11;

/**
 * @date 2019/4/9
 */
public class ThreadException {

    private static final int A = 10;
    private static final int B = 0;

    public static void main(String[] args) {

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5_000);
                int insult = A / B;
                System.out.println(insult);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T");

        t.setUncaughtExceptionHandler((thread, e) -> {
            System.out.println(e.toString());
            System.out.println(thread);
        });

        t.start();
    }


}
