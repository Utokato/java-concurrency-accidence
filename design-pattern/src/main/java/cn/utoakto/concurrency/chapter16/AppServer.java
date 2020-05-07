package cn.utoakto.concurrency.chapter16;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date 2019/5/5
 */
public class AppServer extends Thread {

    private int port;
    private static final int DEFALUT_PORT = 12321;
    private volatile boolean start = true;
    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private ServerSocket server;

    public AppServer() {
        this(DEFALUT_PORT);
    }

    public AppServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            this.server = new ServerSocket(port);
            while (start) {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                threadPool.submit(clientHandler);
                clientHandlers.add(clientHandler);
            }
        } catch (IOException e) {
            // throw new RuntimeException(e);
            // logger
            System.out.println("Sorry, server has shutdown");
        } finally {
            this.dispose();
        }

    }

    private void dispose() {
        this.clientHandlers.forEach(ClientHandler::stop);
        this.threadPool.shutdown();
    }

    public void shutdown() throws IOException {
        this.start = false;
        this.interrupt();
        this.server.close();
        System.out.println("AppServer shutdown!");
    }

}
