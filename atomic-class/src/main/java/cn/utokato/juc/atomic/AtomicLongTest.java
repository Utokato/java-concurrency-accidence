package cn.utokato.juc.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @date 2019/5/16
 */
public class AtomicLongTest {

    /**
     * 总线：数据总线，地址总线，控制总线
     * <p>
     * CPU 可能是 32位或64位
     * Long 是64位，所以传输的时候分为高位(high 32位)和低位(low 32位)
     * {@link AtomicLong#VMSupportsCS8()}
     * 用来获取并记录当前的操作系统是否支持lockless
     * 可以理解为：该变量用来判断是否当前的操作系统能够保证Long高低位操作的原子性，
     * 如果不能保证，JVM会发送一些CPU级别的指令去对总线进行加锁
     * <p>
     * CAS 算法 与 弱CAS算法
     */
    @Test
    public void testCreate() {
        AtomicLong atomicLong = new AtomicLong(100);
        System.out.println(atomicLong);
    }
}
