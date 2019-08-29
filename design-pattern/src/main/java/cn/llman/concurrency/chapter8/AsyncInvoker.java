package cn.llman.concurrency.chapter8;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * {@link Future}           代表的是未来的一个凭据
 * {@link FutureTask}       将调用逻辑进行隔离
 * {@link FutureService}    桥接Future 和 FutureTask
 *
 * @date 2019/5/3
 */
public class AsyncInvoker {
    public static void main(String[] args) throws InterruptedException {
        /*FutureService futureService = new FutureService();
        Future<String> future = futureService.submit(AsyncInvoker::get);

        System.out.println("==========");
        System.out.println("do other things.");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("==========");
        System.out.println(future.get());*/

        FutureService futureService = new FutureService();
        Future<String> future = futureService.submit(AsyncInvoker::get, result-> System.out.println("Callback: "+result));
        System.out.println("==========");
        System.out.println("do other things.");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("==========");
    }

    private static String get() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        return "FINISH";
    }
}
