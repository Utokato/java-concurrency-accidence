package cn.utoakto.concurrency.chapter14;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * @date 2019/4/10
 */
public class TestJavaModifier {


    /**
     * 测试静态代码块、代码块、构造函数的执行
     * <p>
     * 执行顺序为：
     * -    1. 静态代码块
     * -    2. 代码块
     * -    3. 构造函数
     */
    @Test
    public void test1() {
        MyClass1 myClass1 = new MyClass1();
        myClass1.say();
    }

    /**
     * 测试synchronized锁
     * -    实例方法的synchronized锁对象为该实例
     * -    即，多个线程中，每个线程的该实例对象去调用该synchronized方法，并不会出现加锁的现象
     * -    这是由于每个加锁的对象都不同
     */
    @Test
    public void test2() throws InterruptedException {
        Stream.of("T1", "T2", "T3", "T4", "T5").map(name -> new Thread(name) {
            @Override
            public void run() {
                MyClass2 myClass2 = new MyClass2();
                myClass2.sayBefore();
                myClass2.sayAfter();
            }
        }).forEach(Thread::start);

        Thread.sleep(30_000);
    }

    /**
     * 测试synchronized锁
     * -    当一个方法内部的执行流程被一个对象加锁，所有并发的线程执行该方法时，会串行执行
     */
    @Test
    public void test3() throws InterruptedException {
        Stream.of("T1", "T2", "T3", "T4", "T5").map(name -> new Thread(name) {
            @Override
            public void run() {
                MyClass2 myClass2 = new MyClass2();
                myClass2.sayBefore();
                myClass2.say();
            }
        }).forEach(Thread::start);

        Thread.sleep(30_000);
    }

    /**
     * 测试synchronized锁
     * -    多个线程调用一个静态synchronized方法，也会串行执行，此时加锁的对象，是当前类的类文件
     * -    如{@link MyClass2#sayHello()}是一个静态的synchronized方法，
     * -    同时{@link MyClass2#sayHi()}中使用类该类的类文件作为锁
     * -    所以sayHi()和sayHello()串行执行
     */
    @Test
    public void test4() throws InterruptedException {
        Stream.of("T1", "T2", "T3", "T4", "T5").map(name -> new Thread(name) {
            @Override
            public void run() {
                MyClass2 myClass2 = new MyClass2();
                myClass2.sayBefore();
                myClass2.sayHi();
                MyClass2.sayHello();
            }
        }).forEach(Thread::start);

        Thread.sleep(30_000);
    }


}

class MyClass1 {

    /* 测试静态代码块，代码块的执行顺序 */

    {
        System.out.println("this is a common code block"); // 2.
    }

    static {
        System.out.println("this is a static code block"); // 1.
    }

    public MyClass1() {
        System.out.println("Constructor is invocated."); // 3.
    }

    public void say() {
        System.out.println("Hello");
    }
}


/**
 * 静态 synchronized 方法 使用 该类的类文件作为加锁条件
 * 实例 synchronized 方法 使用 该实例自身作为加锁条件
 */
class MyClass2 {

    private final static Object LOCK = new Object();

    public void sayBefore() {
        System.out.println(Thread.currentThread().getName() + " Say sth before...");
    }

    public synchronized void sayAfter() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " Say sth after...");
    }

    public void say() {
        synchronized (LOCK) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " Say sth...");
        }
    }

    public static synchronized void sayHello() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " Say hello...");
    }

    public void sayHi() {
        synchronized (MyClass2.class) {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " Say hi...");
        }
    }

}
