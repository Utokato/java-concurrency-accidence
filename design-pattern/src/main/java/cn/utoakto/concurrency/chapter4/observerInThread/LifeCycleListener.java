package cn.utoakto.concurrency.chapter4.observerInThread;

/**
 * 监听者 接口
 *
 * @date 2019/5/2
 */
public interface LifeCycleListener {
    void onEvent(ObservableRunnable.RunnableEvent event);
}
