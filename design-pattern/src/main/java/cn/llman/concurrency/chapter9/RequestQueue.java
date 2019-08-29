package cn.llman.concurrency.chapter9;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @date 2019/5/3
 */
public class RequestQueue {

    private final LinkedList<Request> queue = new LinkedList<>();


    public Request getRequest() {
        synchronized (queue) {
            while (queue.size() <= 0) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return queue.removeFirst();
        }
    }

    public void putRequest(Request request) {
        synchronized (queue) {
            queue.addLast(request);
            queue.notifyAll();
        }
    }
}
