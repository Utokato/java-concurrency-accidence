package cn.utokato.concurrency.classloader.chapter6;

import cn.utokato.concurrency.classloader.chapter3.MyClassLoader;

/**
 * @date 2019/5/10
 */
public class ThreadContextClassLoader {

    public static void main(String[] args) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);

        Thread.currentThread().setContextClassLoader(new MyClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}
