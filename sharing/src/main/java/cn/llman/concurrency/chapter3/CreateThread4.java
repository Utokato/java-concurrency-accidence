package cn.llman.concurrency.chapter3;

/**
 * @date 2019/4/7
 */
public class CreateThread4 {

    private static int counter = 1;


    public static void main(String[] args) {
        Thread t1 = new Thread(null, new Runnable() {
            @Override
            public void run() {
                try {
                    add(0);
                } catch (Error error) {
                    // error.printStackTrace();
                    System.out.println(counter);
                }
            }

            private void add(int i) {
                counter++;
                // 递归调用，不设置跳出递归的条件，就会导致栈的溢出 StackOverflowError
                add(i + 1);
            }
        }, "Test-Thread", 1 << 24); // 通过手动设置stackSize能够调整虚拟机栈的内存
        // 1<<24 位运算

        t1.start();

    }
}
