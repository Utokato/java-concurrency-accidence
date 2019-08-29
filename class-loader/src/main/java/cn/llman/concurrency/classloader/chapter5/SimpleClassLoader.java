package cn.llman.concurrency.classloader.chapter5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 可以打破类的双亲委派机制
 * 通过{@link SimpleClassLoader#findClass(String)}
 *
 * @date 2019/5/8
 */
public class SimpleClassLoader extends ClassLoader {
    private final static String DEFAULT_DIR = "E:\\@Projects\\java-concurrency-accidence\\class-loader\\src\\main\\app\\revert\\";

    private String dir = DEFAULT_DIR;

    private String classLoaderName;

    public SimpleClassLoader() {
        super();
    }

    public SimpleClassLoader(String classLoaderName) {
        super();
        this.classLoaderName = classLoaderName;
    }

    public SimpleClassLoader(ClassLoader parent, String classLoaderName) {
        super(parent);
        this.classLoaderName = classLoaderName;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;

        // 如果是java的包，让父加载器来加载
        if (name.startsWith("java.")) {
            try {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                clazz = systemClassLoader.loadClass(name);
                if (clazz != null) {
                    if (resolve) resolveClass(clazz);
                    return clazz;
                }
            } catch (Exception e) {
                //
            }
        }

        // 如果是自定义的包，用自己的加载器来加载
        try {
            clazz = findClass(name);
        } catch (Exception e) {
            //
        }

        if (clazz == null && getParent() != null) {
            clazz = getParent().loadClass(name);
        }
        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = name.replace('.', '/');
        File classFile = new File(this.dir, classPath + ".class");

        if (!classFile.exists()) {
            throw new ClassNotFoundException("The class " + name + " not found in " + this.dir);
        }

        byte[] classBytes = loadClassBytes(classFile);
        if (null == classBytes || classBytes.length == 0) {
            throw new ClassNotFoundException("Load the class " + name + " failed");
        }

        return this.defineClass(name, classBytes, 0, classBytes.length);
    }

    private byte[] loadClassBytes(File classFile) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(classFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
            // logger
        }
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getClassLoaderName() {
        return classLoaderName;
    }
}
