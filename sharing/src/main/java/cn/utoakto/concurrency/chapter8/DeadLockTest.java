package cn.utoakto.concurrency.chapter8;

/**
 * @date 2019/4/9
 */
public class DeadLockTest {

    public static void main(String[] args) {
        OtherService otherService = new OtherService();
        DeadLock deadLock = new DeadLock(otherService);
        otherService.setDeadLock(deadLock);

        new Thread(() -> {
            while (true) {
                deadLock.m1();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                otherService.s2();
            }
        }).start();
    }
}
