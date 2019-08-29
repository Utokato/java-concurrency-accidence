package cn.llman.concurrency.chapter3;

/**
 * 由volatile关键引出的很多知识点：
 * 1. CPU 与 CPU cache ，CPU 与 内存结构
 * 2. Java的 内存模型：JMM - 内存缓存机制等
 * 3. 重排序 与 happensBefore
 *
 * @date 2019/4/19
 */
public class VolatileTest {

    /**
     * volatile 关键字
     */
    private volatile static int INIT_VALUE = 0;

    private final static int MAX_LIMIT = 5;


    /**
     * 两个线程
     * 一个线程 读
     * 一个线程 写
     * <p>
     * JMM Java内存模型
     * 以及Java自身对于cache的优化
     * <p>
     * CPU高速缓存与主内存之间的关系
     * 线程在启动时，CPU会从主存中获取需要的数据放到该线程独自的缓存空间中
     * 不同的线程，有不同的缓存空间；
     * 当一个线程只是读取数据时，Java会做出一个不尽人意的优化，即：只从缓存中读取数据，不和主存进行任何交互
     * 当一个线程中有写操作时，会将缓存中新的值刷新到主存中，即：既与缓存交互，又于主存交互
     */
    public static void main(String[] args) {

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (localValue < MAX_LIMIT) {
                if (localValue != INIT_VALUE) {
                    System.out.printf("The value updated to [%d]\n", INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }
        }, "READER").start();

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.printf("Update value to [%d]\n", ++localValue);
                INIT_VALUE = localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            }
        }, "UPDATER").start();

    }

}
