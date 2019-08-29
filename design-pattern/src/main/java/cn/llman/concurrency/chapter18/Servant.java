package cn.llman.concurrency.chapter18;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 包可见 通过工厂类向外提供
 *
 * @date 2019/5/7
 */
class Servant implements ActiveObject {


    @Override
    public Result makeString(int count, char fillChar) {
        char[] buf = new char[count];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = fillChar;
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                // logger
            }
        }
        return new RealResult(new String(buf));
    }


    @Override
    public void displayString(String text) {
        try {
            System.out.println("Display: " + text);
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            // logger
        }
    }
}
