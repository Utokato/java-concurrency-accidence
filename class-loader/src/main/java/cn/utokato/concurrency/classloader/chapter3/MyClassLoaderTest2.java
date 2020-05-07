package cn.utokato.concurrency.classloader.chapter3;


/**
 * 1. 类加载器的委托机制是优先交给父 类加载器先去尝试加载
 * 2. 父加载器和子加载器其实是一种包装关系，或包含关系
 * 3. 如果父加载器加载不了的情况下，再一级一级向下由子加载器去加载；如果所有的加载器都加载不了，就会抛出异常
 * 4. 同一个类经过同一个类加载器的多次加载，只会在方法区产生一个类文件
 * 5. 同一个类经过不同的类加载进行加载，就会产生不同的类文件
 *
 * @date 2019/5/8
 */
public class MyClassLoaderTest2 {
    public static void main(String[] args) throws Exception {
        MyClassLoader classLoader1 = new MyClassLoader("myClassLoader1");
        MyClassLoader classLoader2 = new MyClassLoader(classLoader1, "myClassLoader2");

        classLoader2.setDir("F:\\Intellij_IDEA_Projects\\Accidence\\java-concurrency-accidence\\class-loader\\src\\main\\app\\classloader2\\");

        Class<?> aClass = classLoader2.loadClass("cn.llman.concurrency.classloader.chapter3.MyObject");
        System.out.println(aClass);
        System.out.println(aClass.getClassLoader());
        System.out.println(((MyClassLoader) aClass.getClassLoader()).getClassLoaderName());
        System.out.println("-------------------");


        Class<?> aClass2 = classLoader2.loadClass("cn.llman.concurrency.classloader.chapter3.MyObject");
        System.out.println(aClass.hashCode());
        System.out.println(aClass2.hashCode());
        System.out.println(aClass.equals(aClass2));

    }
}
