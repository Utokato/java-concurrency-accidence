package cn.utokato.concurrency.classloader.chapter4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @date 2019/5/8
 */
public final class EncryptUtils {

    public static final byte ENCRYPT_FACTOR = (byte) 0xff;

    private EncryptUtils() {
        // empty,and don't construct
    }

    public static void doEncrypt(String source, String target) {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(target)) {
            int temp;
            while ((temp = fis.read()) != -1) {
                fos.write(temp ^ ENCRYPT_FACTOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        doEncrypt("",
                "");
    }
}
