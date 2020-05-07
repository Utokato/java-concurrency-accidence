package cn.utoakto.concurrency.chapter3;

/**
 * @date 2019/4/19
 */
public class VolatileTest2 {


    private volatile static int INIT_VALUE = 0;

    private final static int MAX_LIMIT = 20;

    /**
     * 两个线程同时进行写操作时，会将新值从缓存中刷新到主存中，如：
     * 当i=1;执行 i=i+1；
     * T1： MainMemory -> i -> Cache -> i+1 -> Cache[i=2] -> MainMemory[i=2];
     * 当多线程时，T1线程还没有将新值刷新到缓存中时，T2线程也进行了获取了执行权
     * T2： MainMemory -> i -> Cache -> i+1 -> Cache[i=2] -> MainMemory[i=2];
     * <p>
     * 此时，就出现了缓存不一致的情形；为了解决这种缓存不一致的问题，一般有两种解决方案：
     * 1. 给数据总线加锁
     * -    总线(数据总线，地址总线，控制总线)
     * -    LOCK#
     * 2. CPU高速缓存一致性协议
     * -    Intel MESI
     * -    a. 当CPU写入数据的时候，如果发现该变量被共享(也就是说，其他CPU中也存在该变量的一个副本)，
     * -       会发出一个信号，通知其他CPU该变量的缓存无效了
     * -    b. 当其他CPU访问该变量的时候，重新到主内存中进行获取
     */
    public static void main(String[] args) {

        new Thread(() -> {
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.println("ADDER1: " + (++INIT_VALUE));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER1").start();

        new Thread(() -> {
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.println("ADDER2: " + (++INIT_VALUE));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER2").start();

    }
}
