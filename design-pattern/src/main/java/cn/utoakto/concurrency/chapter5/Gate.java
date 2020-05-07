package cn.utoakto.concurrency.chapter5;

/**
 * 共享资源 SharedResource
 * <p>
 * 当多个线程同时去操作一个共享资源时，多个线程需要竞争地去改变共享变量，就会出现异常
 * 为了解决这个问题，可以使用锁机制，synchronized(悲观锁)
 * <p>
 * 对共享资源的写操作，是需要加锁的
 * 对共享资源的读操作，为了提升性能，多个线程同时进行读取操作时，不需要加锁
 * 或者说，需要将读写锁，进行分离
 *
 * @date 2019/5/2
 */
public class Gate {

    private int counter = 0;
    private String name = "Nobody";
    private String address = "Nowhere";

    /**
     * 临界值
     * <p>
     * pass 方法 是 一个写的动作
     *
     * @param name
     * @param address
     */
    public synchronized void pass(String name, String address) {
        /* 竞争 race */
        this.counter++;
        this.name = name;
        this.address = address;
        verify();
    }

    /**
     * verify 方法 中的 toString 是一个读的动作
     */
    private void verify() {
        if (this.name.charAt(0) != this.address.charAt(0)) {
            System.out.println("********BROKEN********" + toString());
        }

    }

    @Override
    public synchronized String toString() {
        return "No." + counter + ": " + name + ", " + address;
    }
}
