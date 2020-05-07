package cn.utokato.concurrency.classloader.chapter5;

/**
 * 打破双亲委派机制
 * {@link SimpleClassLoader#loadClass(String, boolean)} }
 *
 * @date 2019/5/8
 */
public class SimpleClassLoaderTest {

    public static void main(String[] args) throws Exception {
        SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
        Class<?> aClass = simpleClassLoader.loadClass("cn.utokato.concurrency.classloader.chapter5.SimpleObject");
        System.out.println(aClass.hashCode());
        System.out.println(aClass.getClassLoader());

        Class<?> aClass1 = Class.forName("cn.utokato.concurrency.classloader.chapter5.SimpleObject");
        System.out.println(aClass1.hashCode());
        System.out.println(aClass1.getClassLoader());

    }


}
