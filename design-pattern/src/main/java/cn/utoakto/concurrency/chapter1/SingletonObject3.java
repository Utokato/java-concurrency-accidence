package cn.utoakto.concurrency.chapter1;

/**
 * 单例设计模式 - 懒汉式
 *
 * @date 2019/4/10
 */
public class SingletonObject3 {

    private static SingletonObject3 instance;

    private SingletonObject3() {// empty
    }

    /**
     * 可以使用synchronized锁的机制来保证单实例，同时又保证了懒加载
     * 但是又引入了新的问题，以后所有线程获取该单实例时，都变成了串行化，失去了多线程的性能
     * <p>
     * 所以，我们想，能不能在第一次实例化的时候加锁，以后每次获取的时候不加锁
     */
    public synchronized static SingletonObject3 getInstance() {
        if (null == instance) instance = new SingletonObject3();
        return SingletonObject3.instance;
    }
}
