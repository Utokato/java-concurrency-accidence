package cn.utoakto.concurrency.chapter11;

import java.util.Arrays;
import java.util.Optional;

/**
 * {@link Thread#getStackTrace()} 通过getStackTrace方法来获取当前线程的栈追踪信息
 * 如：方法的调用信息
 *
 * @date 2019/4/9
 */
public class Test2 {
    public void test() {
        Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(e -> !e.isNativeMethod())
                .forEach(e -> Optional.of(e.getClassName() + " : " + e.getMethodName() + " : " + e.getLineNumber())
                        .ifPresent(System.out::println));
    }
}
