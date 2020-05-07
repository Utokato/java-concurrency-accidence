package cn.utokato.juc.collections.blocking;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lma
 * @date 2019/06/07
 */
public class AProblemDetails {


    @FunctionalInterface
    interface Invokable {
        void invoke(String msg);
    }

    @FunctionalInterface
    interface Returnable {
        String returea(int msg);
    }

    /**
     * 在{@link Func}类中定义了3个方法：
     * 每个方法都对应了一种类型的函数式接口
     * 1. 没有参数，没有返回值 {@link Runnable} {@link Closeable}等
     * 2. 有参数，没有返回值 {@link Invokable}
     * 3. 有参数，有返回值 {@link Returnable}
     */
    static class Func {
        void foo() {
            System.out.println("do some things");
        }

        void foo(String msg) {
            System.out.println(msg);
        }

        String foo(int msg) {
            return "Get msg: " + msg;
        }
    }

    @Test
    public void testLambda() throws IOException {
        List<Func> list = Arrays.asList(new Func(), new Func());

        /**
         * 通过将 方法引用 func::foo 强制转换为一个函数式接口 {@link Runnable}
         * 对应了{@link Func#foo()} ，没有参数，没有返回值
         *
         * 为什么可以进行这种类型的强制转换呢?
         * 这是因为，这个方法满足了该函数式接口的签名
         * 即: 满足最终方法执行的最终结果，就可以进行强制类型的转换
         * 所有，这里不仅可以强制转换为{@link Runnable}
         * 也可以转换成其他相同签名的函数式接口，如{@link Callable} {@link Closeable}等等
         *
         * 这样做有什么好处呢?
         * 对外进行了屏蔽，{@link Func} 有三个方法，由于每个方法的方法签名不一样
         * 对于不同的方法，可以强制转换为不同类型的函数式接口
         * 外面的调用者只能调用其中的这一个方法，而对其他方法一无所知
         * 这样就做到了通过方法签名来对外进行屏蔽
         *
         */
        Iterator<Runnable> runnableIterator = list.stream().map(func -> (Runnable) func::foo).iterator();
        while (runnableIterator.hasNext()) {
            runnableIterator.next().run();
        }


        /**
         * 通过将 方法引用 func::foo 强制转换为一个函数式接口 {@link Invokable}
         * 对应了{@link Func#foo(String)} ，有参数，没有返回值
         */
        Iterator<Invokable> iterator = list.stream().map(func -> (Invokable) func::foo).iterator();
        while (iterator.hasNext()) {
            iterator.next().invoke("do other things");
        }

        /**
         * 通过将 方法引用 func::foo 强制转换为一个函数式接口 {@link Returnable}
         * 对应了{@link Func#foo(int)} ，有参数，有返回值
         */
        Iterator<Returnable> returnableIterator = list.stream().map(func -> (Returnable) func::foo).iterator();
        while (returnableIterator.hasNext()) {
            String result = returnableIterator.next().returea(new Random().nextInt(100));
            System.out.println(result);
        }


        /**
         * Java8 方法引用的本质：FunctionalInterface 函数式接口的一个实例
         * 方法引用本质上是lambda表达式的语法糖(简写)，lambda就是函数式接口的一个实例
         *
         * 函数式接口
         */
    }

    @Test
    public void test() {
        /* 没有入参，没有返回值 */
        Runnable runnable = () -> System.out.println("sth");

        /* 有入参，没有返回值 - 消费者 */
        Consumer<Integer> consumer = (Integer i) -> System.out.println(i);
        Consumer<Integer> con = System.out::println;

        /* 没有入参，有返回值 - 生产者 */
        Supplier<Integer> supplier = () -> 10;

        /* 有入参，有返回值 */
        Function<Integer, String> function = (Integer i) -> {
            return String.valueOf(i);
        };
        Function<Integer, String> func = String::valueOf;

        /* 有入参，有返回值，返回值仅为Boolean类型 */
        Predicate<Integer> predicate = (Integer i) -> i > 0;
    }

    ////////////
    // 在Java8之前，如何处理这种问题，对一个类中的某些方法进行屏蔽呢?
    ///////////

    /**
     * 这个接口设计不合理，内部有太多的方法
     * 而且这些方法类型不同，有的操作数据，有的管理状态
     * 所以需要将这个接口进行拆分
     */
    interface Management {
        void read();

        void write();

        String state();

        void stop();
    }


    interface Ops {
        void read();

        void write();
    }

    interface Manage {
        String state();

        void stop();
    }

    /**
     * 聚合类
     */
    static class AbstractManagement implements Ops, Manage {

        @Override
        public void read() {

        }

        @Override
        public void write() {

        }

        @Override
        public String state() {
            return null;
        }

        @Override
        public void stop() {

        }
    }

    /**
     * 只保持了{@link Ops}的应用
     * 所以只能使用{@link Ops}接口中定义的方法
     */
    static class OpsImpl {
        private final Ops ops;

        OpsImpl(Ops ops) {
            this.ops = ops;
        }

        public void delegateOps() {
            ops.read();
            ops.write();
        }
    }

    @Test
    public void testManagement() {
        OpsImpl ops = new OpsImpl(new AbstractManagement());
        ops.delegateOps();
    }
}
