package cn.llman.concurrency.chapter5;

/**
 * 场景模拟：
 * 1. 多个线程去采集多个机器上的数据，采集完毕后记录到数据库中
 * 2. 在不用{@link Thread#join()}时，main线程启动后会立马结束，不能等到采集数据的线程执行结束
 * 3. 使用join()方法后，只有当所有的采集数据的线程执行结束后，main线程才会结束。保证了本次采集数据的统一性
 *
 * @date 2019/4/8
 */
public class ThreadJoin3 {

    public static void main(String[] args) throws InterruptedException {

        long startTimeStamp = System.currentTimeMillis();

        Thread t1 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t2 = new Thread(new CaptureRunnable("M2", 30000L));
        Thread t3 = new Thread(new CaptureRunnable("M3", 15000L));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        long endTimeStamp = System.currentTimeMillis();

        System.out.println("Save data begin time stamp is: " + startTimeStamp + ", end time stamp is: " + endTimeStamp);

    }
}


class CaptureRunnable implements Runnable {

    // 采集 机器的名称
    private String machineName;
    // 采集 所消耗的时间
    private long spendTime;


    public CaptureRunnable(String machineName, long spendTime) {
        this.machineName = machineName;
        this.spendTime = spendTime;
    }

    @Override
    public void run() {
        // do the really capture data.
        try {
            Thread.sleep(spendTime);
            System.out.println(machineName + " completed data capture at[" + System.currentTimeMillis() + "] and successfully!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return machineName + " finished!";
    }
}
