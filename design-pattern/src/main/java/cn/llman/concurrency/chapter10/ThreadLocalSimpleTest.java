package cn.llman.concurrency.chapter10;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 在{@link ThreadLocal}中简单的存取值
 * 默认为null，可以设置初始值{@link ThreadLocal#withInitial(Supplier)}
 * {@link ThreadLocal#set(Object)}手动设置值后会覆盖掉初始值
 *
 * @date 2019/5/3
 */
public class ThreadLocalSimpleTest {

    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> "marlonn");

    // JVM start main thread
    public static void main(String[] args) throws InterruptedException {
        // threadLocal.set("Alisa");
        TimeUnit.SECONDS.sleep(1);
        System.out.println(threadLocal.get());

    }
}
