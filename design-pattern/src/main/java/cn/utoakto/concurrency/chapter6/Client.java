package cn.utoakto.concurrency.chapter6;

/**
 * ReadWriteLock Design Pattern
 * Reader-Writer Design Pattern
 *
 * @date 2019/5/2
 */
public class Client {

    public static void main(String[] args) {
        final SharedData sharedData = new SharedData(10);
        new ReaderWorker(sharedData).start();
        new ReaderWorker(sharedData).start();
        new ReaderWorker(sharedData).start();
        new ReaderWorker(sharedData).start();
        new ReaderWorker(sharedData).start();

        new WriterWorker(sharedData, "abcdefjhigklmn").start();
        new WriterWorker(sharedData, "ABCDEFJHIGKLMN").start();

    }
}
