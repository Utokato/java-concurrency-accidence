package cn.utoakto.concurrency.chapter7;

/**
 * @date 2019/5/3
 */
public class ImmutablePerformance {
    public static void main(String[] args) throws InterruptedException {
        long startTimestamp = System.currentTimeMillis();
        SyncObj syncObj = new SyncObj();
        syncObj.setName("Alisa");
        ImmutableObj immutableObj = new ImmutableObj("Alisa");

        Thread t1 = new Thread(() -> doLoop(syncObj));

        Thread t2 = new Thread(() -> doLoop(syncObj));

        t1.start();
        t2.start();
        t1.join();
        t2.join();


        long endTimestamp = System.currentTimeMillis();
        System.out.println("Elapsed time is: " + (endTimestamp - startTimestamp));
    }

    private static void doLoop(Object object) {
        for (long i = 0L; i < 10000000; i++) {
            System.out.println(Thread.currentThread().getName() + " -> " + object.toString());
        }
    }
}


/**
 * 不可变对象
 * <p>
 * 单线程执行1千万次输出使用的时间：Elapsed time is: 32489
 * 两个线程执行分别执行1千万次输出使用的时间：Elapsed time is: 100639
 */
final class ImmutableObj {
    private final String name;

    public ImmutableObj(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ImmutableObj{" +
                "name='" + name + '\'' +
                '}';
    }
}

/**
 * 可变对象，但是其中的方法都是线程安全的(加了锁)
 * <p>
 * 单线程执行1千万次输出使用的时间：Elapsed time is: 32919
 * 两个线程执行分别执行1千万次输出使用的时间：Elapsed time is: 101202
 */
class SyncObj {
    private String name;

    public synchronized void setName(String name) {
        this.name = name;
    }

    @Override
    public synchronized String toString() {
        return "SyncObj{" +
                "name='" + name + '\'' +
                '}';
    }
}
