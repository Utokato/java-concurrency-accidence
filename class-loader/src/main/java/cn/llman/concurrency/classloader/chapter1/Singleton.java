package cn.llman.concurrency.classloader.chapter1;

/**
 * @date 2019/5/7
 */
public class Singleton {

    private static Singleton instance = new Singleton(); // ①

    public static int x = 0;
    public static int y;


    // private static Singleton instance = new Singleton(); // ②

    /**
     * 当private static Singleton instance = new Singleton() 在①的位置时
     * main方法运行的结果为：0 1
     *
     * 两者之间的差异是由于类加载的三个阶段的工作不同导致的
     * 类加载的三个阶段为：
     * -    加载：查找并且加载类的二进制数据
     * -    连接：1. 验证：确保被加载类的正确性
     * -         2. 准备：为类的静态变量分配内存，并将其初始化为默认值
     * -         3. 解析：把类中的符号引用转换为直接引用
     * -    初始化：为类的静态变量赋予正确的初始值
     *
     * 其中最为重要的两步是： 连接的准备阶段 和 初始化阶段
     *
     * 准备阶段：
     * instance = null 默认值为null
     * x = 0 默认值为0
     * y = 0 默认值为0
     * 初始化阶段：
     * instance = new Singleton()
     * -    在构造过程中：x++ 此时x=1
     * -                y++ 此时y=1
     * x=0; 为x进行正常的赋值，将0赋值给x的时候，覆盖了在构造过程中的x++的结果
     * y 没有进行赋值，还是使用构造过程中y++的结果
     *
     * 所有最终输出的结果为 0 1
     */


    /**
     * 当private static Singleton instance = new Singleton() 在②的位置时
     * main方法运行的结果为：1 1
     * <p>
     * 准备阶段：
     * x = 0 默认值为0
     * y = 0 默认值为0
     * instance = null 默认值为null
     * 初始化阶段
     * x=0 将0赋值给x，将默认的0覆盖
     * y 没有进行赋值，还是采用默认值0
     * instance = new Singleton()
     * -    在构造过程中：x++ 此时x=1
     * -                y++ 此时y=1
     * <p>
     * 所以最终输出的结果为 1 1
     */

    private Singleton() {
        x++;
        y++;
    }

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Singleton singleton = getInstance();
        System.out.println(singleton.x);
        System.out.println(singleton.y);
    }
}
