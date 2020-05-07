package cn.utoakto.concurrency.chapter4.observerInThread;

import java.util.Arrays;

/**
 * @date 2019/5/2
 */
public class ThreadLifeCycleClient {
    public static void main(String[] args) {
        new ThreadLifeCycleObserver().concurrentQuery(Arrays.asList("1","2"));
    }
}
