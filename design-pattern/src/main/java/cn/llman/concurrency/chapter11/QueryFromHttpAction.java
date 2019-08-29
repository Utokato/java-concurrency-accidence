package cn.llman.concurrency.chapter11;

import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2019/5/3
 */
public class QueryFromHttpAction {

    public void execute(Context context) {
        String name = context.getName();
        String cardId = getCardIdByName(name);
        context.setCardId(cardId);
    }

    public void execute() {
        Context context = ActionContext.getActionContext().getContext();
        String name = context.getName();
        String cardId = getCardIdByName(name);
        context.setCardId(cardId);
    }

    private String getCardIdByName(String name) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "12345678912541 " + Thread.currentThread().getId();
    }
}
