package cn.llman.concurrency.classloader.chapter4;


import java.lang.reflect.Method;

/**
 * @date 2019/5/8
 */
public class DecryptClassLoaderTest {
    public static void main(String[] args) throws Exception {

        /*MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.setDir("F:\\Intellij_IDEA_Projects\\Accidence\\java-concurrency-accidence\\class-loader\\src\\main\\app\\classloader3\\");

        Class<?> aClass = myClassLoader.loadClass("cn.llman.concurrency.classloader.chapter3.MyObjectWithEncrypt");
        System.out.println(aClass);
        System.out.println(aClass.getClassLoader());*/


        DecryptClassLoader decryptClassLoader = new DecryptClassLoader();
        Class<?> clazz = decryptClassLoader.loadClass("cn.llman.concurrency.classloader.chapter3.MyObject");
        System.out.println(clazz);
        System.out.println(clazz.getClassLoader());

        Object obj = clazz.newInstance();
        Method hello = clazz.getMethod("hello");
        Object result = hello.invoke(obj);
        System.out.println(result);


    }
}
