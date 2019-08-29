package cn.llman.concurrency.chapter3;

import java.util.*;

/**
 * Just for fun
 *
 * @date 2019/4/24
 */
public class OperatorForSet {
    public static void main(String[] args) {
        Iterable<Integer> intersection = getIntersection(Arrays.asList(1, 1, 1, 1, 3, 4, 5, 6, 7, 7, 8), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println(intersection);

        System.out.println("------");

        Iterable<Integer> union = getUnion(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(0, 6, 7, 8, 9));
        System.out.println(union);

        System.out.println("------");

        Iterable<Integer> except = getExcept(Arrays.asList(4, 5, 6, 7, 8, 9), Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(except);

        System.out.println("------");

        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> list3 = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("list1 是 list3 的子集：" + isSubSet(list1, list3));
        System.out.println("list1 是 list3 的真子集：" + isRealSubSet(list1, list3));
        System.out.println("list2 是 list3 的子集：" + isSubSet(list2, list3));
        System.out.println("list2 是 list3 的真子集：" + isRealSubSet(list2, list3));


    }

    /**
     * 获取一个空集
     */
    private static <T> Iterable<T> getEmpty() {
        return new HashSet<>();
    }

    /**
     * s 是 t 的子集吗?
     */
    private static <T> boolean isSubSet(Iterable<T> s, Iterable<T> t) {
        Set<T> result = new HashSet<>();
        getExcept(s, t).forEach(result::add);
        return result.isEmpty();
    }

    /**
     * s 是 t 的真子集吗?
     */
    private static <T> boolean isRealSubSet(Iterable<T> s, Iterable<T> t) {
        List<Object> containerForS = new ArrayList<>();
        List<Object> containerForT = new ArrayList<>();
        if (isSubSet(s, t)) {
            s.forEach(e1 -> containerForS.add(Thread.currentThread()));
            t.forEach(e1 -> containerForT.add(Thread.currentThread()));
            return containerForS.size() == containerForT.size();

        }
        return false;
    }


    /**
     * 集合交集
     */
    private static <T> Iterable<T> getIntersection(Iterable<T> i1, Iterable<T> i2) {
        Set<T> result = new HashSet<>();
        i1.forEach(e1 -> {
            i2.forEach(e2 -> {
                if (e1.equals(e2)) {
                    result.add(e1);
                }
            });
        });
        return result;
    }

    /**
     * 集合并集
     */
    private static <T> Iterable<T> getUnion(Iterable<T> i1, Iterable<T> i2) {
        Set<T> result = new HashSet<>();
        i1.forEach(e1 -> {
            result.add(e1);
            i2.forEach(result::add);
        });
        return result;
    }

    /**
     * 集合差集
     */
    private static <T> Iterable<T> getExcept(Iterable<T> expect, Iterable<T> despair) {
        Set<T> result = new HashSet<>();
        expect.forEach(e1 -> {
            result.add(e1);
            despair.forEach(e2 -> {
                if (e1.equals(e2)) {
                    result.remove(e1);
                }
            });
        });
        return result;
    }

}

