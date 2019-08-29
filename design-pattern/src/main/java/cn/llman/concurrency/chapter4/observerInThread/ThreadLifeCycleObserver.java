package cn.llman.concurrency.chapter4.observerInThread;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 监听者 类
 *
 * @date 2019/5/2
 */
public class ThreadLifeCycleObserver implements LifeCycleListener {

    private final Object LOCK = new Object();


    /**
     * @param ids 线程的id(名称)
     */
    public void concurrentQuery(List<String> ids) {
        if (ids == null || ids.isEmpty()) return;
        ids.forEach(id -> new Thread(new ObservableRunnable(this) {
            @Override
            public void run() {
                try {
                    notifyChange(new RunnableEvent(RunnableState.RUNNING, Thread.currentThread(), null));
                    System.out.println("Query for the id: " + id);
                    // int x = 1 / 0;
                    TimeUnit.MILLISECONDS.sleep(1_000L);
                    notifyChange(new RunnableEvent(RunnableState.DONE, Thread.currentThread(), null));
                } catch (Exception e) {
                    notifyChange(new RunnableEvent(RunnableState.ERROR, Thread.currentThread(), e));
                }
            }
        }, id).start());
    }


    @Override
    public void onEvent(ObservableRunnable.RunnableEvent event) {
        synchronized (LOCK) {
            System.out.println("The runnable [" + event.getThread().getName() + "] changed and state is [" + event.getState() + "].");
            if (event.getCause() != null) {
                System.out.println("The runnable [" + event.getThread().getName() + "] process failed.");
                event.getCause().printStackTrace();
            }
        }
    }
}
