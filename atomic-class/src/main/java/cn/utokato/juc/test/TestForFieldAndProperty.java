package cn.utokato.juc.test;

import java.util.function.Function;

/**
 * 区分 Java 中的字段和属性
 *
 * @author lma
 * @date 2019/06/22
 */
public class TestForFieldAndProperty {

    public static void main(String[] args) {
        Student student = new Student();
        student.id = 10;
        student.score = 100;

        System.out.println(student.id + "->" + student.score);

        Function<Integer,Integer> func =a -> a * a;
        Integer result = func.apply(2);
        System.out.println(result);

    }


}

class Student {
    public int id; // 字段
    public int score;

    private String name; // 属性

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
