package cn.llman.concurrency.chapter11;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class QueryFromDBAction {

    public void execute(Context context) {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            String name = "Alisa" + Thread.currentThread().getName();
            context.setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            String name = "Alisa" + Thread.currentThread().getName();
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
