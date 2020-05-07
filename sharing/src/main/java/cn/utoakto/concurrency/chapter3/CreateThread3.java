package cn.utoakto.concurrency.chapter3;

/**
 * @date 2019/4/7
 */
public class CreateThread3 {

    private int i = 0;
    private byte[] bytes = new byte[1024];
    private static int counter = 0;


    // JVM will create a thread named "main"
    public static void main(String[] args) {
        // create a JVM stack

        // local variable
        // int j = 0;
        // int[] arr = new int[1024];


        try {
            add(0);
        } catch (Error error) {
            // error.printStackTrace();
            System.out.println(counter); // ~12262
        }


    }

    private static void add(int i) {
        ++counter;
        add(i + 1);
    }
}


// StackOverflowError 栈溢出
