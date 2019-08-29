package cn.llman.concurrency.chapter7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @date 2019/5/3
 */
public class ImmutableTest {

    private final int age;
    private final String name;
    private final List<String> list;

    public ImmutableTest(int age, String name) {
        this.age = age;
        this.name = name;
        list = new ArrayList<>();
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public List<String> getList() {
        /* 为了使该对象不可变，将list置为不可修改的对象 */
        return Collections.unmodifiableList(list);
    }
}
