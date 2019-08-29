package cn.llman.juc.test;

public class TestEnum {
    public static void main(String[] args) {
        Level l1 = Level.LOW;
        Level l2 = Level.MIDDLE;
        Level l3 = Level.HIGH;
        System.out.println(l1);
        int i = 10;
    }
}

enum Level{
    LOW,
    MIDDLE,
    HIGH
}


