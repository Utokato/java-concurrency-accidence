package cn.utoakto.concurrency.chapter1;

/**
 * @date 2019/4/7
 */
public class TryConcurrency {

    public static void main(String[] args) {
        // readFromDataBase();
        // writeDataToFile();

        /*for (int i = 0; i < 100; i++) {
            println("Task i: " + i);
        }*/


        // 当我们new Thread();时，它与我们一个普通的Java实例一样，并不是一个线程
        // 只有当你启动这个Thread实例时，它才会成为一个线程，并且是一个立即启动(不会阻塞)的线程
       /* Thread t1 = new Thread("Custom-Thread") {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    println("Task i: " + i);
                }
            }
        };
        t1.start();*/

        /*for (int j = 0; j < 1000; j++) {
            println("Task j: " + j);
        }*/

        readAndWriteData();

    }

    private static void readAndWriteData() {
        new Thread("Read-Thread") {
            @Override
            public void run() {
                println(Thread.currentThread().getName());
                readFromDataBase();
            }
        }.start();

        new Thread("Write-Thread") {
            @Override
            public void run() {
                writeDataToFile();
            }
        }.start();

    }


    private static void readFromDataBase() {
        // read data from database and handle it.
        try {
            println("Begin reading data from db.");
            Thread.sleep(1000 * 10L);
            println("Read data done and start to handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The data handle finished and successfully.");
    }

    private static void writeDataToFile() {
        // write data to file.
        try {
            println("Begin writing data to file.");
            Thread.sleep(2000 * 10L);
            println("Write data done and start to handle it.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        println("The data handle finished and successfully.");
    }


    private static void println(String msg) {
        System.out.println(msg);
    }
}
