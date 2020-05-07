package cn.utokato.concurrency.classloader.chapter1;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @date 2019/5/8
 */
public class ClassInitialThreadTest {

    public static void main(String[] args) {

        new Thread(SimpleObject::new).start();

        new Thread(SimpleObject::new).start();

    }


    static class SimpleObject {
        private static AtomicBoolean init = new AtomicBoolean(true);

        static {
            System.out.println(Thread.currentThread().getName() + ", I will be initial.");
            while (init.get()) {

            }
            System.out.println(Thread.currentThread().getName() + ", I finish initial.");
        }
    }

}

