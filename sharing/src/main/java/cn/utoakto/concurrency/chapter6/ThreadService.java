package cn.utoakto.concurrency.chapter6;

/**
 * 配合关闭一个线程
 *
 * @date 2019/4/8
 */
public class ThreadService {

    private Thread executeThread;

    private Boolean finished = false; // 判断task是否已经执行结束

    public void execute(Runnable task) {
        executeThread = new Thread(() -> {
            Thread runner = new Thread(task);
            runner.setDaemon(true);
            runner.start();
            try {
                // 这里执行join后，executeThread线程就会等待runner线程的执行
                // runner线程的执行如果超时，就可以打断executeThread线程
                // 由于runner线程是一个守护线程，一旦executeThread线程终止，runner线程也会随之终止
                runner.join();
                finished = true;
                System.out.println("Task is performing normally ");
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        });

        executeThread.start();
    }

    public void shutdown(Long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if ((System.currentTimeMillis() - currentTime) > mills) {
                System.out.println("任务超时，需要强制关闭!");
                executeThread.interrupt();
                break;
            }

            if (Thread.interrupted()) break;
        }
        finished = false;
    }

}
