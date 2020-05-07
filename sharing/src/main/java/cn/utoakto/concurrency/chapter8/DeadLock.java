package cn.utoakto.concurrency.chapter8;

/**
 * 测试死锁
 *
 * @date 2019/4/9
 */
public class DeadLock {

    private final Object lock = new Object();

    private OtherService otherService;

    public DeadLock(OtherService otherService) {
        this.otherService = otherService;
    }

    public void m1() {
        synchronized (lock) {
            System.out.println("m1=========>");
            otherService.s1();
        }
    }


    public void m2() {
        synchronized (lock) {
            System.out.println("m2=========>");
        }
    }
}
