package java.lang;

/**
 * 当我们自定义一个 java.lang.String 类时
 * 这个类不会生效，因为它与rt包下的冲突了，由于父委托机制的存在
 * BootClassLoader会加载rt包下的类，所以这个类是不会生效的
 *
 * @date 2019/5/8
 */
public class String {

    static {
        System.out.println("执行static代码块");
    }
}
