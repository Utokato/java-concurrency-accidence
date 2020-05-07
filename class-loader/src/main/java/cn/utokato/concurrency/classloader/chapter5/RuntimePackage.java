package cn.utokato.concurrency.classloader.chapter5;

/**
 * @date 2019/5/9
 */
public class RuntimePackage {

    // RuntimePackage
    // cn.llman.concurrency.classloader.chapter5
    // Boot.Ext.App.cn.llman.concurrency.classloader.chapter5

    // Boot.Ext.App.cn.llman.concurrency.classloader.chapter5.RuntimePackage
    // Boot.Ext.App.SimpleClassLoader.cn.llman.concurrency.classloader.chapter5.RuntimePackage


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        SimpleClassLoader classLoader = new SimpleClassLoader();
        Class<?> aClass = classLoader.loadClass("cn.utokato.concurrency.classloader.chapter5.SimpleObject");
        System.out.println(aClass.getClassLoader());


        /**
         * Exception in thread "main" java.lang.ClassCastException:
         * cn.llman.concurrency.classloader.chapter5.SimpleObject
         * cannot be cast to
         * cn.llman.concurrency.classloader.chapter5.SimpleObject
         *
         * 下面的代码会报错
         * 包的命名空间不一样
         * RuntimePackage的命名空间为：Boot.Ext.App.cn.llman.concurrency.classloader.chapter5.RuntimePackage
         * SimpleObject的命名空间为： Boot.Ext.App.SimpleClassLoader.cn.llman.concurrency.classloader.chapter5.SimpleObject
         * 由于命名空间的不一致，导致各个加载器之间加载的类相互不可见，所以报错
         */
        SimpleObject simpleObject = (SimpleObject) aClass.newInstance();
    }

}
