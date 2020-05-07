package cn.utoakto.concurrency.chapter1;

import java.util.stream.IntStream;

/**
 * 单例设计模式 - 懒汉式
 *
 * @date 2019/4/10
 */
public class SingletonObject4 {

    private static SingletonObject4 instance;

    private Object obj;

    /**
     * JVM的优化(如：重排序等)如何引起异常?
     * 比如：在这个单例的构造函数中有很多引用，当JVM创建完单实例对象，但其中的很多引用暂时还没有得到初始化时，
     * 该执行线程失去了CPU执行权
     * 当下次后者后面的线程进来获取单实例时，返现单实例已经不为null了，就直接返回了该单实例
     * 但它们不知道该单实例并没有初始化完成，所以可能引起空指针异常
     * <p>
     * 注：JVM 为了优化程序的执行，只保证结果的最终一致性，在真正赋值(引用)时，并不保证先后顺序
     * 如：int i = 0;int j = 10; 只保证最终的结果，至于是先给i赋值，还是先给j赋值，JVM并不保证该顺序
     */
    private SingletonObject4() {
        int i = 0;
        int j = 10;
        obj = new Object();
    }

    /**
     * 双重检查 double check，实现了单实例，懒加载，唯一性，但可能引起空指针异常
     * <p>
     * 但是这种模式下，也会因为JVM的优化(如：重排序等)引起异常
     */
    public static SingletonObject4 getInstance() {
        if (null == instance) {
            synchronized (SingletonObject4.class) {
                if (null == instance) instance = new SingletonObject4();
            }
        }

        return SingletonObject4.instance;
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100).forEach(i -> new Thread(String.valueOf(i)) {
            @Override
            public void run() {
                System.out.println(SingletonObject4.getInstance());
            }
        }.start());
    }
}
