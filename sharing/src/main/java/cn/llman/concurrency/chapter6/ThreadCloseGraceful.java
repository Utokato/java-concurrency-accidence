package cn.llman.concurrency.chapter6;

/**
 * 如何优雅的关闭一个线程?
 * 通过一个flag的方式
 *
 * @date 2019/4/8
 */
public class ThreadCloseGraceful {

    private static class Worker extends Thread {
        private volatile boolean start = true;

        @Override
        public void run() {
            while (start) {
                // to do anything
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("I am in hard work...");
            }
        }

        public void shotdown() {
            this.start = false;
        }
    }


    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();


        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.shotdown();
    }
}
