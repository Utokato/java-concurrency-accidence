package cn.llman.concurrency.chapter4;

import java.util.Optional;

/**
 * @date 2019/4/8
 */
public class ThreadSimpleApi {


    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            Optional.of("Hello").ifPresent(System.out::println);
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");


        t1.start();
        Optional.of(t1.getName()).ifPresent(System.out::println);
        Optional.of(t1.getId()).ifPresent(System.out::println); // 根据内部线程数量从0开始++
        Optional.of(t1.getPriority()).ifPresent(System.out::println); // 设置优先级可以试图改变该线程的执行优先级，但不一定生效


    }
}
