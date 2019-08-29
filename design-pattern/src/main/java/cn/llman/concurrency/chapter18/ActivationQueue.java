package cn.llman.concurrency.chapter18;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.awt.font.TextHitInfo;
import java.util.LinkedList;

/**
 * @date 2019/5/7
 */
public class ActivationQueue {


    private final static int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

    private final LinkedList<MethodRequest> methodQueue;

    public ActivationQueue(LinkedList<MethodRequest> methodQueue) {
        this.methodQueue = methodQueue;
    }

    public synchronized void put(MethodRequest methodRequest) {
        while (methodQueue.size() >= MAX_METHOD_REQUEST_QUEUE_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // logger
            }
        }
        this.methodQueue.addLast(methodRequest);
        this.notifyAll();
    }

    public synchronized MethodRequest take() {
        while (methodQueue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                // logger
            }
        }
        MethodRequest methodRequest = methodQueue.removeFirst();
        this.notifyAll();
        return methodRequest;
    }
}
