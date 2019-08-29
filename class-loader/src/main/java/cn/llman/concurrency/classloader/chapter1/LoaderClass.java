package cn.llman.concurrency.classloader.chapter1;

/**
 * @date 2019/5/8
 */
public class LoaderClass {

    public static void main(String[] args) {
        /*MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();
        MyObject myObject3 = new MyObject();
        MyObject myObject4 = new MyObject();

        System.out.println(myObject1.getClass() == myObject2.getClass());
        System.out.println(myObject1.getClass() == myObject3.getClass());
        System.out.println(myObject1.getClass() == myObject4.getClass());*/

        System.out.println(MyObject.x);
        System.out.println(MyObject.y);

        // System.out.println(Sub.i);
    }
}

class MyObject {

    public static int x = 0;

    /**
     * 在初始化阶段，静态语句块中只能访问到定义在静态语句块之前的变量(读取、赋值)
     * 而，定义在静态语句块之后的变量，只能进行赋值操作，不能进行读取操作
     * todo why
     */
    static {
        System.out.println("在静态代码块中 读取x的值为：" + x);
        x = x + 1;

        y = 200 + 1;
        System.out.println("在静态代码块中 y 被赋值为：201");
        // System.out.println(y); //这就话会报错，illegal forward reference
    }

    public static int y = 0;
}

class Parent {

    static {
        System.out.println("Parent");
    }


}

class Sub extends Parent {

    public static int i = 1000;

    static {
        System.out.println("Child");
    }
}
