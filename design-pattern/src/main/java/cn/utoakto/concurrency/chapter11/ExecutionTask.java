package cn.utoakto.concurrency.chapter11;

/**
 * @date 2019/5/3
 */
public class ExecutionTask implements Runnable {

    private QueryFromDBAction dbAction = new QueryFromDBAction();
    private QueryFromHttpAction httpAction = new QueryFromHttpAction();


    @Override
    public void run() {
        // final Context context = new Context();
        // dbAction.execute(context);
        // httpAction.execute(context);
        Context context = ActionContext.getActionContext().getContext();
        dbAction.execute();
        System.out.println("Query name successful.");
        httpAction.execute();
        System.out.println("Query card ID successful.");
        System.out.println("Username is: " + context.getName() + ", and card ID is: " + context.getCardId() + ".");
    }


}
