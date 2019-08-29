package cn.llman.concurrency.classloader.chapter1;

import java.util.Random;

/**
 * @date 2019/5/7
 */
public class ClassActiveUse {
    public static void main(String[] args) throws ClassNotFoundException {

        // new Obj();
        // System.out.println(I.a);
        // System.out.println(Obj.salary);
        // Obj.printSalary();
        // Class.forName("cn.llman.concurrency.classloader.Obj");
        // System.out.println(Child.age);


        // (1) 特殊情形：通过子类去调用父类的静态变量，只有父类会进行类加载，子类不会进行初始化
        // 注意：静态变量不属于任何一个类，而放置在静态变量表中
        // System.out.println(Child.salary);

        // (2) 特殊情形：构造一个对象(引用)的数组，该class并不会被初始化
        // Obj[] objs = new Obj[10];

        // (3) 特殊情形：调用一个类的静态常量，即被final修饰，该class不会被初始化
        // 注意：常量在编译阶段就会被放到常量池中
        // System.out.println(Obj.money);

        // (4) 特殊情形：调用一个类的静态常量，但是该常量是一个随机数(复杂类型)，在编译期间无法计算得出，需要在运行时才会被确定下来
        // 所以 这种情况下class会被初始化
        System.out.println(Obj.x);
    }
}

class Obj {

    public static long salary = 10000;
    public final static int x = new Random().nextInt(100);

    public final static double money = 12.3d;

    static {
        System.out.println("Obj 被初始化");
    }

    public static void printSalary() {
        System.out.println("===>Salary: " + salary);
    }
}

class Child extends Obj {
    public static int age = 30;

    static {
        System.out.println("Child 被初始化");
    }

}


// 访问某个类或接口的静态变量，或者对该静态变量进行赋值操作
// 1. 对某个类的静态变量进行读写 -> class
// 2. 对接口的静态变量进行读取 -> interface

interface I {
    // 接口中变量 都是 public final static
    int a = 1;
}
