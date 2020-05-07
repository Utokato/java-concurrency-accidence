package cn.utoakto.concurrency.chapter1;

/**
 * 单例设计模式 - 懒汉式
 *
 * @date 2019/4/10
 */
public class SingletonObject2 {

    private static SingletonObject2 instance;

    private SingletonObject2() {// empty
    }

    /**
     * 多线程模式下，无法保证这里instance的单实例性质
     * 当第一个线程通过了if的判断，还没有执行单实例的构造时，失去了CPU的执行权
     * 第二个线程也就能通过if的判断，然后实例化一个单实例
     * 当第一个线程再次获得CPU执行权时，并不知道有另外的线程实例化了对象，所以会继续实例化该单实例
     * 所以，整个应用程序中的单实例就不能保证唯一
     */
    public static SingletonObject2 getInstance() {
        if (null == instance) instance = new SingletonObject2();
        return SingletonObject2.instance;
    }
}
