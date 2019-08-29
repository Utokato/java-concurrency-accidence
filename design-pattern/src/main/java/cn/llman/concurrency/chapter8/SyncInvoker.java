package cn.llman.concurrency.chapter8;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/3
 */
public class SyncInvoker {

    /**
     * 同步调用中，由于被调用方的阻塞，导致了调用方的阻塞
     */
    public static void main(String[] args) throws InterruptedException {
        String result = get();
        System.out.println(result);

    }

    private static String get() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        return "FINISH";
    }
}
