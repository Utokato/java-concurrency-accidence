package cn.utoakto.concurrency.chapter17;


import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @date 2019/5/7
 */
public class Channel {

    private final static int MAX_REQUEST = 100;

    private final Request[] requestQueue;

    private int head;
    private int tail;
    private int count;
    private final WorkerThread[] workerPool;


    public Channel(int workers) {
        this.requestQueue = new Request[MAX_REQUEST];
        this.workerPool = new WorkerThread[workers];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.init();
    }

    private void init() {
        IntStream.range(0, workerPool.length).forEach(i -> {
            WorkerThread workerThread = new WorkerThread("Worker-" + i, this);
            workerPool[i] = workerThread;
        });
    }

    public void startWorker() {
        Arrays.stream(workerPool).forEach(WorkerThread::start);
    }

    public synchronized void put(Request request) {
        while (count >= requestQueue.length) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        this.requestQueue[tail] = request;
        this.tail = (this.tail + 1) % requestQueue.length;
        this.count++;
        this.notifyAll();
    }

    public synchronized Request take() {
        while (count <= 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        Request request = this.requestQueue[head];
        this.head = (this.head + 1) % requestQueue.length;
        this.count--;
        this.notifyAll();
        return request;
    }
}
