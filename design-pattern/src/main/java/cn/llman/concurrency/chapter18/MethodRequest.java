package cn.llman.concurrency.chapter18;

/**
 * 对应于 {@link ActiveObject} 的每一个方法
 *
 * @date 2019/5/7
 */
public abstract class MethodRequest {

    protected final Servant servant;
    protected final FutureResult futureResult;

    public MethodRequest(Servant servant, FutureResult futureResult) {
        this.servant = servant;
        this.futureResult = futureResult;
    }

    public abstract void execute();

}

