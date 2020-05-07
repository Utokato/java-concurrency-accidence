package cn.utokato.juc.utils.forkjoin;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @date 2019/5/23
 */
public class ForkJoinRecursiveAction {

    private final static int MAX_THRESHOLD = 3;

    private final static AtomicInteger SUM = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new CalculatedRecursiveAction(0, 10));
        pool.awaitTermination(10, TimeUnit.SECONDS);
        Optional.of(SUM).ifPresent(System.out::println);
    }

    private static class CalculatedRecursiveAction extends RecursiveAction {

        private final int start;
        private final int end;

        private CalculatedRecursiveAction(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if ((end - start) <= MAX_THRESHOLD) {
                SUM.addAndGet(IntStream.rangeClosed(start, end).sum());
            } else {
                int middle = (start + end) / 2;
                CalculatedRecursiveAction leftTask = new CalculatedRecursiveAction(start, middle);
                CalculatedRecursiveAction rightTask = new CalculatedRecursiveAction(middle + 1, end);

                leftTask.fork();
                rightTask.fork();
            }
        }
    }

}
