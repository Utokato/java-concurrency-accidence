package cn.llman.concurrency.chapter1;

/**
 * 单例设计模式 - 饿汉式
 *
 * 缺点在于：不能懒加载
 *
 * @date 2019/4/10
 */
public class SingletonObject1 {

    private static final SingletonObject1 instance = new SingletonObject1();

    private SingletonObject1() {
    }

    public static SingletonObject1 getInstance() {
        return instance;
    }

}
