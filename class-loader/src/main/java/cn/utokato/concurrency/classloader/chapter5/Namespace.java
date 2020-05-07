package cn.utokato.concurrency.classloader.chapter5;

/**
 * @date 2019/5/9
 */
public class Namespace {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = Namespace.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("java.lang.String");
        Class<?> bClass = classLoader.loadClass("java.lang.String");

        System.out.println(aClass.hashCode());
        System.out.println(bClass.hashCode());
        System.out.println(aClass.equals(bClass));
    }
}
