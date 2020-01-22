package cn.llman.juc.atomic;

import org.junit.Test;
import sun.misc.Unsafe;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * @date 2019/5/16
 */
public class UnsafeFoolTest {

    public static void main(String[] args) throws Exception {
        /*Simple simple = new Simple();
        System.out.println(simple.get());*/

        // Simple simple = Simple.class.newInstance();

        /*Class<?> clazz = Class.forName("cn.llman.juc.atomic.UnsafeFoolTest");
        System.out.println(clazz);*/

        Unsafe unsafe = getUnsafe();
        /**
         * 使用{@link Unsafe#allocateInstance(Class)}开绕过构造函数去开辟一个实例对象空间
         * 这是很危险的操作
         */
        Simple simple = (Simple) unsafe.allocateInstance(Simple.class);
        System.out.println(simple);
        System.out.println(simple.get());
        System.out.println(simple.getClass());
        System.out.println(simple.getClass().getClassLoader());
    }

    @Test
    public void testSizeOf(){
        System.out.println(sizeOf(new Simple()));
    }

    private static long sizeOf(Object obj) {
        Unsafe unsafe = getUnsafe();
        Set<Field> fields = new HashSet<>();
        Class<?> clazz = obj.getClass();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }

        long maxOffSet = 0;
        for (Field field : fields) {
            long offSet = unsafe.objectFieldOffset(field);
            if (offSet > maxOffSet) {
                maxOffSet = offSet;
            }
        }

        return ((maxOffSet / 8) + 1) * 8;

    }

    @Test
    public void testUnsafePutInt() throws NoSuchFieldException {

        Guard guard = new Guard();
        guard.work();
        System.out.println("======");

        Field field = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
        Unsafe unsafe = getUnsafe();
        /**
         * 使用{@link Unsafe#putInt(Object, long, int)}
         * 可以为某一个属性强制赋值
         * 即使这个变量是私有的，并且没有对外部暴露setter方法
         * 这种操作也属于危险操作
         */
        unsafe.putInt(guard, unsafe.objectFieldOffset(field), 42);
        guard.work();
    }

    /**
     * 通过{@link Unsafe#defineClass(String, byte[], int, int, ClassLoader, ProtectionDomain)}
     * 来加载一个类
     */
    @Test
    public void testUnsafeLoadClass() throws Exception {
        byte[] bytes = loadClassContent();
        Unsafe unsafe = getUnsafe();
        Class<?> clazz
                = unsafe.defineClass(null, bytes, 0, bytes.length, null, null);
        int result
                = (int) clazz.getMethod("get").invoke(clazz.newInstance(), null);
        System.out.println(result);
    }


    private static byte[] loadClassContent() throws Exception {

        File file = new File("......");
        FileInputStream fis = new FileInputStream(file);

        byte[] content = new byte[(int) file.length()];
        fis.read(content);
        fis.close();
        return content;
    }

    static class Guard {
        private int ACCESS_ALLOWED = 1;

        private boolean allow() {
            return 42 == ACCESS_ALLOWED;
        }

        public void work() {
            if (allow()) {
                System.out.println("I am working by allowed!");
            }
        }
    }

    static class Simple {
        private long l = 0;

        public Simple() {
            this.l = 1;
            System.out.println("========");
        }

        public long get() {
            return l;
        }
    }

    /**
     * 通过反射获取Unsafe类的实例
     */
    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



}
