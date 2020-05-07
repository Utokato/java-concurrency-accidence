package cn.utoakto.concurrency.chapter11;

/**
 * 使用{@link ThreadLocal}来处理context上下文环境
 * 每一个线程的threadLocal中绑定了当前线程需要执行的上下文环境
 *
 * @date 2019/5/3
 */
public class ActionContext {

    /**
     * 禁止构造
     */
    private ActionContext() {
    }

    /**
     * 这里有一个问题值得注意：
     * 当在一个线程池中，有多个线程
     * 当一个线程执行完逻辑单元时，会在它的ThreadLocal中存储一些数据
     * 当下次这个线程再次执行其他逻辑单元时，需要清理掉ThreadLocal中的历史遗留数据
     */
    private static final ThreadLocal<Context> threadLocal = ThreadLocal.withInitial(Context::new);

    /**
     * 单例模式
     */
    private static class ContextHolder {
        private final static ActionContext actionContext = new ActionContext();
    }

    public static ActionContext getActionContext() {
        return ContextHolder.actionContext;
    }

    public Context getContext() {
        return threadLocal.get();
    }
}
