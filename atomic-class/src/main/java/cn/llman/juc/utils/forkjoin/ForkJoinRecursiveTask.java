package cn.llman.juc.utils.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * @date 2019/5/23
 */
public class ForkJoinRecursiveTask {

    private final static int MAX_THRESHOLD = 200;

    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> future = pool.submit(new CalculateRecursiveTask(0, 1000));
        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class CalculateRecursiveTask extends RecursiveTask<java.lang.Integer> {

        private final int start;
        private final int end;

        CalculateRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        protected java.lang.Integer compute() {
            if (end - start <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(start, end).sum();
            } else {
                int middle = (start + end) / 2;
                CalculateRecursiveTask leftTask = new CalculateRecursiveTask(start, middle);
                CalculateRecursiveTask rightTask = new CalculateRecursiveTask(middle + 1, end);
                leftTask.fork();
                rightTask.fork();
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
