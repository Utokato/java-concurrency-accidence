package cn.utokato.juc.collections.concurrency;

import org.junit.Test;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * {@link ConcurrentSkipListMap}
 *
 * @author lma
 * @date 2019/06/09
 */
public class ConcurrentSkipListMapExample {

    public static <K, V> ConcurrentSkipListMap<K, V> create() {
        return new ConcurrentSkipListMap<>();
    }

    /**
     * round 四舍五入
     * ceiling 向上取整
     * floor 向下取整
     */
    @Test
    public void testCeiling() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        assertThat(map.size(), equalTo(3));
        assertThat(map.ceilingKey(2), equalTo(5));
        assertThat(map.ceilingEntry(2).getValue(), equalTo("Java"));
        assertThat(map.ceilingEntry(5).getValue(), equalTo("Java"));

    }

    @Test
    public void testFloor() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        assertThat(map.size(), equalTo(3));
        assertThat(map.floorKey(2), equalTo(1));
        assertThat(map.floorEntry(2).getValue(), equalTo("Scala"));
        assertThat(map.floorEntry(1).getValue(), equalTo("Scala"));

    }

    @Test
    public void testFirst() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        assertThat(map.firstKey(), equalTo(1));
        assertThat(map.firstEntry().getValue(), equalTo("Scala"));
    }

    @Test
    public void testLast() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        assertThat(map.lastKey(), equalTo(10));
        assertThat(map.lastEntry().getValue(), equalTo("Kotlin"));
    }

    @Test
    public void testMerge() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        /**
         * {@link ConcurrentSkipListMap#merge(Object, Object, BiFunction)}
         * BiFunction 中，(oldValue, newValue)->{return ""}
         * oldValue： map中的数据，如 Scala
         * newValue： 本次需要merge的数据，如 C++
         */
        String result = map.merge(1, "C++", (oldValue, newValue) -> {
            assertThat(oldValue, equalTo("Scala"));
            assertThat(newValue, equalTo("C++"));
            return oldValue + newValue;
        });

        assertThat(result, equalTo("ScalaC++"));
        assertThat(map.get(1), equalTo("ScalaC++"));
    }

    @Test
    public void testCompute() {
        ConcurrentSkipListMap<Integer, String> map = create();

        map.put(1, "Scala");
        map.put(5, "Java");
        map.put(10, "Kotlin");

        String result = map.compute(1, (k, v) -> {
            assertThat(k, equalTo(1));
            assertThat(v, equalTo("Scala"));
            return "Hello,Scala";
        });

        assertThat(result, equalTo("Hello,Scala"));
        assertThat(map.get(1), equalTo("Hello,Scala"));


    }
}


