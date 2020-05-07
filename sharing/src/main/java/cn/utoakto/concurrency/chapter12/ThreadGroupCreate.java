package cn.utoakto.concurrency.chapter12;


import java.util.Arrays;

/**
 * @date 2019/4/10
 */
public class ThreadGroupCreate {

    public static void main(String[] args) {
        // use the name
        ThreadGroup tg1 = new ThreadGroup("TG1");
        Thread t1 = new Thread(tg1, "t1") {
            @Override
            public void run() {
                try {
                    System.out.println("The group of t1 is: " + getThreadGroup().getName());
                    System.out.println("The parent of group of t1 is: " + getThreadGroup().getParent());
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();

        System.out.println("-----------------");


        // use the parent thread group and name
        ThreadGroup tg2 = new ThreadGroup("TG2");
        // System.out.println(tg2.getName());
        // System.out.println(tg2.getParent());
        Thread t2 = new Thread(tg2, "T2") {
            @Override
            public void run() {
                System.out.println(">>>" + tg1.getName());

                Thread[] threads = new Thread[tg1.activeCount()];
                tg1.enumerate(threads);
                Arrays.stream(threads).forEach(System.out::println);

            }
        };
        t2.start();

        // System.out.println(Thread.currentThread().getName());
        // System.out.println(Thread.currentThread().getThreadGroup());
    }
}
