package cn.llman.juc.utils.exchanger;

import java.util.concurrent.Exchanger;

/**
 * @date 2019/5/20
 */
public class ExchangerExample2 {

    /**
     * Exchanger 传递的对象与接收的对象是同一个对象
     * <p>
     * A thread will send the object: java.lang.Object@149cc210
     * B thread will send the object: java.lang.Object@c25b5ef
     * B has received the object:     java.lang.Object@149cc210
     * A has received the object:     java.lang.Object@c25b5ef
     * <p>
     * 这就需要考虑到线程安全的问题，比如说：传递的是一个集合，集合本身的地址没有改变
     * 但是集合内部的元素是可以发生改变的
     */
    public static void main(String[] args) {

        final Exchanger<Object> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                Object aObj = new Object();
                System.out.println("A thread will send the object: " + aObj);
                Object rObj = exchanger.exchange(aObj);
                System.out.println("A has received the object: " + rObj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "A").start();

        new Thread(() -> {
            try {
                Object bObj = new Object();
                System.out.println("B thread will send the object: " + bObj);
                Object rObj = exchanger.exchange(bObj);
                System.out.println("B has received the object: " + rObj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "B").start();

    }
}
