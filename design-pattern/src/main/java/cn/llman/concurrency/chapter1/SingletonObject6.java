package cn.llman.concurrency.chapter1;


import java.util.stream.IntStream;

/**
 * 单例设计模式
 *
 * @date 2019/4/10
 */
public class SingletonObject6 {


    private SingletonObject6() {
    }

    private static class InstanceHolder {
        private final static SingletonObject6 instance = new SingletonObject6();
    }

    public static SingletonObject6 getInstance() {
        return InstanceHolder.instance;
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100).forEach(i -> new Thread(String.valueOf(i)) {
            @Override
            public void run() {
                System.out.println(SingletonObject6.getInstance());
            }
        }.start());
    }
}
