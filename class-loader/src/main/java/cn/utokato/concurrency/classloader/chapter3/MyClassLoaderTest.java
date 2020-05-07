package cn.utokato.concurrency.classloader.chapter3;

import java.lang.reflect.Method;

/**
 * @date 2019/5/8
 */
public class MyClassLoaderTest {
    public static void main(String[] args) throws Exception {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> aClass = classLoader.loadClass("cn.llman.concurrency.classloader.chapter3.MyObject");
        System.out.println(aClass);
        System.out.println(aClass.getClassLoader());
        System.out.println(aClass.getClassLoader().getParent());
        System.out.println(aClass.getClassLoader().getParent().getParent());
        System.out.println("-------------------------");

        Object obj = aClass.newInstance();
        Method hello = aClass.getMethod("hello");
        Object result = hello.invoke(obj);
        System.out.println(result);
    }
}
