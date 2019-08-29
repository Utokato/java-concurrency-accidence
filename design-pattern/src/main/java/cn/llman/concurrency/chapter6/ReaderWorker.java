package cn.llman.concurrency.chapter6;

/**
 * @date 2019/5/2
 */
public class ReaderWorker extends Thread {

    private final SharedData data;


    public ReaderWorker(SharedData data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] result = data.read();
                System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(result));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
