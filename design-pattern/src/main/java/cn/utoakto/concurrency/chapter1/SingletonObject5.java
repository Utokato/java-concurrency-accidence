package cn.utoakto.concurrency.chapter1;

import java.util.stream.IntStream;

/**
 * 单例设计模式 - 懒汉式
 *
 * @date 2019/4/10
 */
public class SingletonObject5 {

    /**
     * volatile 保证了执行的内存可见性和顺序性
     * 禁止编译器的优化和重排序
     */
    private static volatile SingletonObject5 instance;


    private SingletonObject5() {
    }

    /**
     * double check  + volatile
     */
    public static SingletonObject5 getInstance() {
        if (null == instance) {
            synchronized (SingletonObject5.class) {
                if (null == instance) instance = new SingletonObject5();
            }
        }

        return SingletonObject5.instance;
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100).forEach(i -> new Thread(String.valueOf(i)) {
            @Override
            public void run() {
                System.out.println(SingletonObject5.getInstance());
            }
        }.start());
    }
}
