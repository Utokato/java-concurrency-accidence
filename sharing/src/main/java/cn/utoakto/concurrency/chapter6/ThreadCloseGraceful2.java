package cn.utoakto.concurrency.chapter6;

/**
 * 如何优雅的关闭一个线程?
 * 通过{@link Thread#interrupt()}实现
 *
 * @date 2019/4/8
 */
public class ThreadCloseGraceful2 {

    private static class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                if (Thread.interrupted()) break;
                System.out.println("I am in hard work, now thread is not interrupted");

            }
            // --- do another things
            System.out.println("Do another things.");
        }

    }


    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.interrupt();
    }
}
