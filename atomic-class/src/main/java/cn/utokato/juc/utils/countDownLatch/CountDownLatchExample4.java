package cn.utokato.juc.utils.countDownLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/19
 */
public class CountDownLatchExample4 {

    private final static Random random = new Random(System.currentTimeMillis());

    static class Event {
        int id = 0;

        public Event(int id) {
            this.id = id;
        }
    }

    interface Watcher {
        // void startWatch();

        void done(Table table);
    }

    static class TaskBatch implements Watcher {
        private CountDownLatch latch;
        private TaskGroup taskGroup;

        public TaskBatch(TaskGroup taskGroup, int size) {
            this.taskGroup = taskGroup;
            this.latch = new CountDownLatch(size);
        }

        @Override
        public void done(Table table) {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("The table " + table.tableName + " finished work, [ " + table + " ]");
                taskGroup.done(table);
            }
        }
    }

    static class TaskGroup implements Watcher {
        private CountDownLatch latch;
        private Event event;

        public TaskGroup(int size, Event event) {
            this.latch = new CountDownLatch(size);
            this.event = event;
        }

        @Override
        public void done(Table table) {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("==> All of table done in event: " + event.id);
            }
        }
    }

    static class Table {
        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name='t_a'><column name='col_a' type='varchar' /></table>";
        String targetColumnSchema = "";

        public Table(String tableName, long sourceRecordCount) {
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "tableName='" + tableName + '\'' +
                    ", sourceRecordCount=" + sourceRecordCount +
                    ", targetCount=" + targetCount +
                    ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                    ", targetColumnSchema='" + targetColumnSchema + '\'' +
                    '}';
        }
    }

    private static List<Table> capture(Event event) {
        ArrayList<Table> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Table("event-" + event.id + " table-" + "-" + i, i * 1000));
        }
        return list;
    }

    public static void main(String[] args) {
        Event[] events = {new Event(1), new Event(2)};
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (Event event : events) {
            List<Table> tables = capture(event);
            TaskGroup taskGroup = new TaskGroup(tables.size(), event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(taskGroup, 2);
                TrustSourceRecordCount recordCountRunnable = new TrustSourceRecordCount(table, taskBatch);
                TrustSourceColumns columnsRunnable = new TrustSourceColumns(table, taskBatch);
                threadPool.execute(columnsRunnable);
                threadPool.execute(recordCountRunnable);
            }
        }
    }

    static class TrustSourceRecordCount implements Runnable {
        private final Table table;
        private final TaskBatch batch;

        TrustSourceRecordCount(Table table, TaskBatch batch) {
            this.table = table;
            this.batch = batch;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetCount = table.sourceRecordCount;
            // System.out.println("The table[ " + table.tableName + " ] target record count capture done and update the data!");
            batch.done(table);
        }
    }

    static class TrustSourceColumns implements Runnable {
        private final Table table;
        private final TaskBatch batch;

        TrustSourceColumns(Table table, TaskBatch batch) {
            this.table = table;
            this.batch = batch;
        }

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            table.targetColumnSchema = table.sourceColumnSchema;
            // System.out.println("The table[ " + table.tableName + " ] target columns capture done and update the data!");
            batch.done(table);
        }
    }
}
