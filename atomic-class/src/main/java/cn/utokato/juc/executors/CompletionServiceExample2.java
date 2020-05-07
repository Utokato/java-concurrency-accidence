package cn.utokato.juc.executors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @date 2019/5/28
 */
public class CompletionServiceExample2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // testTakeAndPool();
        testTakeAndPool1();
    }

    private static void testTakeAndPool1() throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        ExecutorCompletionService<Event> completionService = new ExecutorCompletionService<>(service);
        final Event event = new Event(1);
        completionService.submit(new MyTask(event), event);
        Future<Event> eventFuture = completionService.take();
        System.out.println(eventFuture.get().getResult());
    }

    private static class MyTask implements Runnable {

        private final Event event;

        private MyTask(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            nap(10);
            event.setResult("I am successful.");
        }
    }

    private static class Event {
        private final int eventId;
        private String result;

        private Event(int eventId) {
            this.eventId = eventId;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getEventId() {
            return eventId;
        }

        public String getResult() {
            return result;
        }
    }

    private static void testTakeAndPool() throws InterruptedException, ExecutionException {
        List<Callable<Integer>> callableList = Arrays.asList(() -> {
            nap(11);
            System.out.println("The 10 finished!");
            return 10;
        }, () -> {
            nap(21);
            System.out.println("The 20 finished!");
            return 20;
        });
        ExecutorService service = Executors.newFixedThreadPool(2);
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(service);

        List<Future<Integer>> futures = new ArrayList<>();

        callableList.forEach(callable -> futures.add(completionService.submit(callable)));

       /* Future<Integer> future;
        while ((future = completionService.take()) != null) {
            System.out.println(future.get());
        }*/

        Future<Integer> future = completionService.poll(15, TimeUnit.SECONDS);
        System.out.println(future.get());
    }

    private static void nap(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
