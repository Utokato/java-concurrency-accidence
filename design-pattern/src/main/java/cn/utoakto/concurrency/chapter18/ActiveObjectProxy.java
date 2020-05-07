
package cn.utoakto.concurrency.chapter18;

/**
 * 代理类
 * 包可见 通过工厂类向外提供
 *
 * @date 2019/5/7
 */
class ActiveObjectProxy implements ActiveObject {

    private final ScheduleThread scheduleThread;
    private final Servant servant;

    public ActiveObjectProxy(ScheduleThread scheduleThread, Servant servant) {
        this.scheduleThread = scheduleThread;
        this.servant = servant;
    }

    @Override
    public Result makeString(int count, char fillChar) {
        FutureResult futureResult = new FutureResult();
        this.scheduleThread.invoke(new MakeStringRequest(servant, futureResult, count, fillChar));
        return futureResult;
    }

    @Override
    public void displayString(String text) {
        this.scheduleThread.invoke(new DisplayStringRequest(servant, text));
    }
}
