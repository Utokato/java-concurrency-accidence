package cn.utoakto.concurrency.chapter7;

/**
 * 测试this锁
 * 当一个类中的多个方法都被synchronized修饰，一瞬间只能执行其中的一个方法
 *
 * 如：在{@link ThisLock}中定义了3个方法，其中m1和m3都被synchronized修饰，m2被一个自定义的对象加锁
 * 在测试中，同时运行3个线程，一瞬间只有m1和m3其中的一个方法运行，当然m2的锁不一样，所以能执行
 *
 * @date 2019/4/9
 */
public class SynchronizedThisLock {
    public static void main(String[] args) {

        ThisLock thisLock = new ThisLock();

        new Thread("T1") {
            @Override
            public void run() {
                thisLock.m1();
            }
        }.start();

        new Thread("T2") {
            @Override
            public void run() {
                thisLock.m2();
            }
        }.start();

        new Thread("T3") {
            @Override
            public void run() {
                thisLock.m3();
            }
        }.start();
    }


}

class ThisLock {

    private final Object LOCK = new Object();

    public synchronized void m1() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void m2() {
        synchronized (LOCK) {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void m3() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
