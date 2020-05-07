package cn.utoakto.concurrency.chapter7;

/**
 * @date 2019/5/2
 */
public class StringTest {
    public static void main(String[] args) {
        String s1 = "Hello";
        String s2 = s1.replace('l', 'k');
        System.out.println(s1.getClass() + " - " + s1.hashCode());
        System.out.println(s2.getClass() + " - " + s2.hashCode());
    }
}
