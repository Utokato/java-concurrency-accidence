package cn.llman.juc.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @date 2019/5/20
 */
public class ExchangerExample1 {

    /**
     * {@link Exchanger#exchange(Object)} 用于两个线程之间相互传递数据
     * exchange() 方法的参数，是要传递给另外一个线程的数据，返回值是另外一个线程给传递过来的数据
     * exchange() 可以设置超时时间，超过了时间就会抛出TimeoutException异常
     * 一旦exchange一方出现了异常，另一方就会陷入无限的等待中
     * 这是因为，Exchanger设计用来两个线程之间的数据传递，必须有pair线程(一对线程)的存在才行
     * <p>
     * 所以，在使用Exchanger时，但一方陷入阻塞时，另一方即使数据已经准备好了，也会被动陷入阻塞状态
     * 只有Exchanger的两端都将数据准备好，才能交换成功，完成本次数据的传递
     * <p>
     * 另外，Exchanger只能保证线程的成对出现，并且多个线程之间，没有办法保证指定线程与指定线程之间的数据交换
     * <p>
     * ==A== start.
     * ==B== start.
     * ==D== start.
     * ==C== start.
     * ==D== get returned result [ I come from ==A== ]
     * ==A== get returned result [ I come from ==D== ]
     * ==A== end.
     * ==D== end.
     * ==B== get returned result [ I come from ==C== ]
     * ==B== end.
     * ==C== get returned result [ I come from ==B== ]
     * ==C== end.
     * <p>
     * 当4个线程同时运行Exchanger，没有办法保证指定线程之间的数据交换
     * 当有奇数个线程同时运行Exchanger时，总有一个线程落单，最终使得整个程序陷入阻塞，无法退出
     * 这是由于落单的线程，没有和它进行数据交换的线程，所有已知处于waiting的状态
     * <p>
     * 综上：尽量保证两个线程来执行Exchanger，这样能保证数据交换的可靠性
     */
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start.");
                String returnResult = exchanger.exchange("I come from " + Thread.currentThread().getName(), 10, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " get returned result [ " + returnResult + " ]");
                System.out.println(Thread.currentThread().getName() + " end.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                System.out.println("Time out");
            }
        }, "==A==").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start.");
                TimeUnit.MILLISECONDS.sleep(9);
                String returnResult = exchanger.exchange("I come from " + Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + " get returned result [ " + returnResult + " ]");
                System.out.println(Thread.currentThread().getName() + " end.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "==B==").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start.");
                TimeUnit.MILLISECONDS.sleep(11);
                String returnResult = exchanger.exchange("I come from " + Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + " get returned result [ " + returnResult + " ]");
                System.out.println(Thread.currentThread().getName() + " end.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "==C==").start();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " start.");
                TimeUnit.MILLISECONDS.sleep(10);
                String returnResult = exchanger.exchange("I come from " + Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + " get returned result [ " + returnResult + " ]");
                System.out.println(Thread.currentThread().getName() + " end.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "==D==").start();

    }
}
