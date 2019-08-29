package cn.llman.concurrency.classloader.chapter4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @date 2019/5/8
 */
public class DecryptClassLoader extends ClassLoader {

    private final static String DEFAULT_DIR = "F:\\Intellij_IDEA_Projects\\Accidence\\java-concurrency-accidence\\class-loader\\src\\main\\app\\classloader3\\";

    private String dir = DEFAULT_DIR;

    public void setDir(String dir) {
        this.dir = dir;
    }

    public DecryptClassLoader(ClassLoader parent) {
        super(parent);
    }

    public DecryptClassLoader() {
        super();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = name.replace('.', '\\');
        File classFile = new File(this.dir, classPath + ".class");

        if (!classFile.exists()) {
            throw new ClassNotFoundException("The class " + name + " not found in " + this.dir);
        }

        byte[] classBytes = loadClassByte(classFile);
        if (null == classBytes || classBytes.length == 0) {
            throw new ClassNotFoundException("Load the class " + name + " failed");
        }

        return this.defineClass(name, classBytes, 0, classBytes.length);


    }

    private byte[] loadClassByte(File classFile) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(classFile)) {
            int temp;
            while ((temp = fis.read()) != -1) {
                baos.write(temp ^ EncryptUtils.ENCRYPT_FACTOR);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
            // logger
        }

    }
}
