package cn.llman.concurrency.chapter18;

/**
 * @date 2019/5/7
 */
public class ScheduleThread extends Thread {

    private final ActivationQueue activationQueue;

    public ScheduleThread(ActivationQueue activationQueue) {
        this.activationQueue = activationQueue;
    }

    public void invoke(MethodRequest methodRequest) {
        this.activationQueue.put(methodRequest);
    }

    @Override
    public void run() {
        while (true) {
            this.activationQueue.take().execute();
        }
    }
}
