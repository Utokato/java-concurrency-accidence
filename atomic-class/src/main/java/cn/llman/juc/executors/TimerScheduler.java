package cn.llman.juc.executors;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/27
 */
public class TimerScheduler {

    /**
     * Scheduler 定时任务(调度任务)的解决方案
     * -    Timer/TimerTask
     * -    ScheduledExecutorService
     * -    crontab
     * -    cron4j
     * -    quartz
     */
    public static void main(String[] args) {
        Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("*****" + System.currentTimeMillis());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
}
