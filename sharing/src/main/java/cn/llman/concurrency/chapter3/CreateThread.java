package cn.llman.concurrency.chapter3;

/**
 * @date 2019/4/7
 */
public class CreateThread {

    public static void main(String[] args) {
        Thread t0 = new Thread();
        Thread t1 = new Thread(() -> System.out.println("Say something ..."));
        t0.start();
        t1.start();
        System.out.println(t0.getName());
        System.out.println(t1.getName());


        /* ----------------------- */

        Thread t2 = new Thread("myThread");
        Thread t3 = new Thread(() -> System.out.println("Running ..."));
        t2.start();
        t3.start();
        System.out.println(t2.getName());
        System.out.println(t3.getName());


        Thread t4 = new Thread(() -> System.out.println("Runnable ..." + Thread.currentThread().getName()), "RunnableThread");
        t4.start();
        System.out.println(t4.getName());


    }
}
