package cn.llman.concurrency.chapter4;

import java.util.Optional;

/**
 * 测试线程的优先级，不建议使用
 * <p>
 * 如果想要实现某些任务先执行，可以使用优先队列
 *
 * @date 2019/4/8
 */
public class ThreadSimpleApi2 {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Optional.of(Thread.currentThread().getName() + ":->index" + i).ifPresent(System.out::println);
            }
        });
        t1.setPriority(Thread.MAX_PRIORITY); // 设置为最高优先级

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Optional.of(Thread.currentThread().getName() + ":->index" + i).ifPresent(System.out::println);
            }
        });
        t2.setPriority(Thread.NORM_PRIORITY); // 设置为一般优先级

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Optional.of(Thread.currentThread().getName() + ":->index" + i).ifPresent(System.out::println);
            }
        });
        t3.setPriority(Thread.MIN_PRIORITY); // 设置为最低优先级


        t1.start();
        t2.start();
        t3.start();
    }
}
