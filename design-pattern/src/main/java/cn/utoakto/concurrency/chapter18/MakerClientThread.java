package cn.utoakto.concurrency.chapter18;

import java.util.concurrent.TimeUnit;

/**
 * @date 2019/5/7
 */
public class MakerClientThread extends Thread {

    private final ActiveObject activeObject;
    private final char fillChar;

    public MakerClientThread(String name, ActiveObject activeObject) {
        super(name);
        this.activeObject = activeObject;
        this.fillChar = name.charAt(0);
    }

    @Override
    public void run() {
        try {
            for (int i = 0; true; i++) {
                Result result = activeObject.makeString(i + 1, fillChar);
                TimeUnit.MILLISECONDS.sleep(20);
                String resultValue = (String) result.getResultValue();
                System.out.println(Thread.currentThread().getName() + ": value = " + resultValue);
            }
        } catch (InterruptedException e) {
            // logger
        }
    }
}
