package cn.utoakto.concurrency.chapter16;

import java.io.*;
import java.net.Socket;

/**
 * @date 2019/5/5
 */
public class ClientHandler implements Runnable {

    private final Socket socket;

    private volatile boolean running = true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             PrintWriter writer = new PrintWriter(outputStream)) {
            while (running) {
                String msg = reader.readLine();
                if (msg == null) break;
                System.out.println("Come from client -> " + msg);
                writer.write("echo: " + msg + "\n");
                writer.flush();
            }

        } catch (IOException e) {
            // logger
            this.running = false;
        } finally {
            this.stop();
        }

    }

    public void stop() {
        if (!running) {
            return;
        }

        this.running = false;
        try {
            this.socket.close();
            System.out.println("ClientHandler stopped!");
        } catch (IOException e) {
            // logger
        }

    }
}
