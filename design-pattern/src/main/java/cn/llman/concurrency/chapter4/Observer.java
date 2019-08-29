package cn.llman.concurrency.chapter4;

/**
 * @date 2019/5/2
 */
public abstract class Observer {
    protected Subject subject;

    public Observer(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    public abstract void update();
}
