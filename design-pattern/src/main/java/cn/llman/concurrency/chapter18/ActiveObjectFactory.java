package cn.llman.concurrency.chapter18;

import java.util.LinkedList;

/**
 * 工厂类
 *
 * @date 2019/5/7
 */
public final class ActiveObjectFactory {
    private ActiveObjectFactory() {
    }

    public static ActiveObject createActiveObject() {
        Servant servant = new Servant();
        ActivationQueue activationQueue = new ActivationQueue(new LinkedList<>());
        ScheduleThread scheduleThread = new ScheduleThread(activationQueue);
        ActiveObjectProxy activeObjectProxy = new ActiveObjectProxy(scheduleThread, servant);
        scheduleThread.start();
        return activeObjectProxy;
    }
}
