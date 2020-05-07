package cn.utokato.concurrency.classloader.chapter2;

/**
 * @date 2019/5/8
 */
public class BootClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        // System.out.println(System.getProperty("sun.boot.class.path"));
        // System.out.println(System.getProperty("java.ext.dirs"));

        /*Properties properties = System.getProperties();
        // System.out.println(properties);
        properties.forEach((key, value) -> {
            System.out.println(key + " ==> " + value);
        });*/

        Class<?> aClass = Class.forName("cn.utokato.concurrency.classloader.chapter2.SimpleObject");
        System.out.println(aClass.getClassLoader());
        System.out.println(aClass.getClassLoader().getParent());
        System.out.println(aClass.getClassLoader().getParent().getParent());
        System.out.println("-------------------------------------");
        Class<?> aClass1 = Class.forName("java.lang.String");
        System.out.println(aClass1);
        System.out.println(aClass1.getClassLoader());
    }
}
