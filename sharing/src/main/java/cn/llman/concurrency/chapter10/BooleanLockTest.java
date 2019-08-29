package cn.llman.concurrency.chapter10;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @date 2019/4/9
 */
public class BooleanLockTest {


    public static void main(String[] args) {
        final BooleanLock lock = new BooleanLock();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(() -> {
            Collection<Thread> threads = lock.getBlockedThread();
            System.err.println("size is: " + threads.size());
            threads.forEach(t -> System.out.print(t.getName() + " "));
        }, 0, 1, TimeUnit.SECONDS);

        Stream.of("T1", "T2", "T3", "T4", "T5").forEach(name -> {
            new Thread(() -> {
                try {
                    lock.lock(20_000);
                    Optional.of(Thread.currentThread().getName() + " get the lock monitor.")
                            .ifPresent(System.out::println);
                    work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Lock.TimeOutException e) {
                    Optional.of("I catch a exception that is " + Thread.currentThread().getName() + " time out").ifPresent(System.out::println);

                } finally {
                    lock.unlock();
                }
            }, name).start();
        });


    }

    private static void work() {
        try {
            Optional.of(Thread.currentThread().getName() + " is working...")
                    .ifPresent(System.out::println);
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
