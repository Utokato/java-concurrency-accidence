package cn.llman.concurrency.chapter9;

import java.util.concurrent.TimeUnit;

/**
 * Guarded Suspension design pattern
 * <p>
 * 保证挂起的设计模式
 * 本质上还是消息队列的应用
 * 当一个线程正在执行其他单元时，有其他的执行单元发送过来
 * 此时，需要将这些其他的执行单元加入到一个队列中
 * 当该线程执行完手上的单元时，去队列中获取新的执行单元进行执行
 * <p>
 * 总计：
 * 可执行单元 -> 队列 -> 执行线程(线程池)
 * <p>
 * 多个请求并发时，线程池中的线程数量(1个或多个)远小于并发请求数量
 * 此时，需要消息队列来作为缓冲，将多余的请求存放在消息队列中，并给这些请求返回一个future
 * 线程池中的线程从消息队列中取出请求，进行处理
 * 处理完毕后，通过钩子函数(回调函数)将结果返回给发起请求的客户端
 *
 * @date 2019/5/3
 */
public class SuspendTest {

    public static void main(String[] args) throws InterruptedException {
        final RequestQueue queue = new RequestQueue();
        new ClientThread(queue, "hha").start();
        ServerThread serverThread = new ServerThread(queue);
        serverThread.start();
        TimeUnit.SECONDS.sleep(20);
        serverThread.close();
    }
}
