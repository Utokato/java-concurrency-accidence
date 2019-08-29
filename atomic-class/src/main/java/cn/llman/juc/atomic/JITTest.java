package cn.llman.juc.atomic;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/12
 */
public class JITTest {

    private static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (!flag) {
                // System.out.println("...");
            }
            /**
             * 这里有一个很诡异的现象，而且不同版本的JDK、或不同的JVM下测试结果也不同，这是由于JIT捣的鬼
             *
             * 当flag变量没有被volatile修饰时，并且while循环内部没有逻辑单元时
             * JIT 会将这段代码编译为
             * while(true){} 所以当flag被另外一个线程修改时，这个while循环并不受影响，会一直运行下去
             *
             * 当flag变量没有被volatile修饰时，并且while循环内部有逻辑单元时
             * JIT 不会对代码进行优化
             * 编译完成后还是原来的代码，所以另外的线程修改flag时，这个线程也会受到影响
             *
             * 当flag变量被volatile修饰时，该变量在内存中是可见的，所以不会出现问题
             *
             * 所以在多线程的环境下，需要一个开关变量时，为了稳妥起见，这个变量应使用volatile修饰
             * 或者使用{@link java.util.concurrent.atomic.AtomicBoolean}
             *
             *
             * Just In Time 即时编译技术
             *
             */
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            flag = true;
            System.out.println("Set flag to true.");
        }).start();
    }
}
