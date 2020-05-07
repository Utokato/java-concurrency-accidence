package cn.utoakto.concurrency.chapter1;

/**
 * TemplateMethod 模板方法
 * {@link Thread} Java的Thread类中的start()方法的设计使用了这种模板方法
 * 在模板中定义了start()方法执行的逻辑流程，但对于需要开发者实现的部分，抽象出来了一个run()方法
 * 保证了执行逻辑的同时，又不失灵活性
 * 作为开发者，只需要在new Thread()时，覆写其中的run()方法，然后调用start()的时候，就会执行到自己定义的run()方法
 * 这是一个很巧妙的设计模式，经常用作于框架执行顺序的开发，如spring的refresh()方法
 *
 * @date 2019/4/7
 */
public class TemplateMethod {

    public final void print(String msg) {
        System.out.println("##################");
        warpPrint(msg);
        System.out.println("##################");
    }

    protected void warpPrint(String msg) {
    }

    public static void main(String[] args) {
        TemplateMethod t1 = new TemplateMethod() {
            @Override
            protected void warpPrint(String msg) {
                System.out.println("*" + msg + "*");
            }
        };

        t1.print("Hello thread");

        TemplateMethod t2 = new TemplateMethod() {
            @Override
            protected void warpPrint(String msg) {
                System.out.println("+" + msg + "+");
            }
        };

        t2.print("Hello thread");


    }


}
