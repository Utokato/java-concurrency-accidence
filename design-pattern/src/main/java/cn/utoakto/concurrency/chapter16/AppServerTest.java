package cn.utoakto.concurrency.chapter16;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/5
 */
public class AppServerTest {
    public static void main(String[] args) throws InterruptedException, IOException {

        AppServer appServer = new AppServer(13345);
        appServer.start();

        TimeUnit.SECONDS.sleep(15);
        appServer.shutdown();


    }
}
