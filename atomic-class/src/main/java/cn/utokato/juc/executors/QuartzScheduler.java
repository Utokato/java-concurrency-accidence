package cn.utokato.juc.executors;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @date 2019/5/27
 */
public class QuartzScheduler {

    /**
     * {@link SimpleJob} 需要执行的定时任务的具体逻辑
     * <p>
     * 支持corn表达式，与corntab一样
     * 即使定时任务是比较耗时的任务，也会根据cron设定的时间来调度执行
     * <p>
     * 比如，定时的任务需要执行1min，而cron中执行这个任务每个30s执行一次
     * 就会启动多个线程来继续执行该调度任务，每个30s执行一次
     */
    public static void main(String[] args) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("Job1", "Group1")
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("Trigger1", "Group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);


    }

}
