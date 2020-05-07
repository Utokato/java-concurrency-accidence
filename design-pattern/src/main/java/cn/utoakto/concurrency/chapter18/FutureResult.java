package cn.utoakto.concurrency.chapter18;

/**
 * @date 2019/5/7
 */
public class FutureResult implements Result {

    private Result result;

    private boolean ready = false;

    public synchronized void setResult(Result result) {
        this.result = result;
        this.ready = true;
        this.notifyAll();
    }


    @Override
    public synchronized Object getResultValue() {
        while (!ready) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // logger
            }
        }
        return this.result.getResultValue();
    }
}
