package cn.utoakto.concurrency.chapter3;

/**
 * @date 2019/4/8
 */
public class CreateThread5 {

    private static int counter = 1;

    public static void main(String[] args) {
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                counter++;
                new Thread(() -> {
                    while (true) {
                        // to do nothing.
                        byte[] data = new byte[1024 * 1024 * 2]; // 设置一个2M的数据空间
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Error error) {

        }

        System.out.println("Total created threads num: " + counter);
    }
}
