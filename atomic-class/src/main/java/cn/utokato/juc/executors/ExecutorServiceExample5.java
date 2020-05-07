package cn.utokato.juc.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @date 2019/5/28
 */
public class ExecutorServiceExample5 {

    /**
     * {@link ThreadPoolExecutor#getQueue()} 可以直接获取线程池中任务队列
     * 可以手动地向该任务队列中添加runnable任务，那么线程池是否会执行这些手动提交的任务呢?
     * 换个问法：手动向任务队列中添加任务，和通过submit、execute方法向线程池中提交任务有什么区别呢?
     * <p>
     * 通过submit、execute方法向线程池中提交任务时，会触发线程池去激活线程，这些线程会从任务队列中获取任务，然后去执行
     * 而手动地向任务队列中添加任务，不会触发线程池激活线程，但一个新的线程池中activeThread为0时，手动提交的任务不能执行
     * <p>
     * 当时，当线程池中有activeThread时，activeThread会从任务队列中获取任务去执行，我们手动提交的任务也可以得到执行
     * <p>
     * 如下实验中，{@link ThreadPoolExecutor#prestartCoreThread()}可以激活一个线程
     * 之后我们再去手动获取Queue提交任务，任务是可以得到执行的
     */
    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        // executorService.prestartCoreThread();
        executorService.getQueue().add(() -> {
            System.out.println("I am added by manual");
        });


    }
}
