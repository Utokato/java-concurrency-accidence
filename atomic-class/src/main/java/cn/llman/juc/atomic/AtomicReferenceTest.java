package cn.llman.juc.atomic;

import org.omg.CORBA.OBJ_ADAPTER;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @date 2019/5/16
 */
public class AtomicReferenceTest {

    public static void main(String[] args) {
        SimpleObject simpleObject = new SimpleObject("A", 10);
        AtomicReference<SimpleObject> arObject = new AtomicReference<>(simpleObject);
        System.out.println(arObject.get().toString());

        boolean result = arObject.compareAndSet(simpleObject, new SimpleObject("B", 11));
        boolean result2 = arObject.compareAndSet(simpleObject, new SimpleObject("C", 12));

        System.out.println(result);
        System.out.println(result2);
        System.out.println(arObject.get());

        JButton jButton = new JButton();

        SimpleObject object = new SimpleObject("D", 14);
        // 这里将AtomicReference作为一个自定义对象的包装
        // 在匿名内部类中的变量是不能进行修改的，即实质上这些变量都被final修饰了
        // 这种情况下，我们想要在匿名内部类中修改一个变量时，可以再写一个新的包装类
        // 包装类不发生改变，但内部包含的自定义对象已经可以修改了
        // AtomicReference 在这里起到的就是包装类的作用
        AtomicReference<SimpleObject> atrObject = new AtomicReference<>(object);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // invoke restful service
                atrObject.set(new SimpleObject("E", 15));
            }
        });

    }

    static class ObjectWarp<T> {
        private T t;

        public ObjectWarp(T t) {
            this.t = t;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }


    static class SimpleObject {
        private String name;
        private int age;

        public SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "SimpleObject{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
