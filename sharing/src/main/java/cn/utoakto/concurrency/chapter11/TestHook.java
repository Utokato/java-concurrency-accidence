package cn.utoakto.concurrency.chapter11;

/**
 * {@link Runtime#addShutdownHook(Thread)}
 * 通过该类可以获取每一个Java应用运行时的一些环境等参数
 * 这里利用addShutdownHook为该应用添加一个钩子函数
 * 当应用程序抛出{@link RuntimeException}时，会执行钩子函数中的程序
 * 所以，我们在hook函数中可以进行一个释放资源、通知开发人员的操作
 * 当主程序已经出现了运行时异常，钩子函数通过新开启一个线程来执行逻辑
 * <p>
 * 体会hook的使用
 * 区分 受检异常{@link Exception} 和 运行时异常{@link RuntimeException}
 *
 * @date 2019/4/9
 */
public class TestHook {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("The application will exit..");
            notifyAndRelease();
        }, "Hook"));

        int i = 0;
        while (true) {
            try {
                Thread.sleep(1_000);
                System.out.println("I am working + " + i);
            } catch (InterruptedException e) {
                // ignored
            }
            i++;
            // 模拟运行了一段时间出现了异常
            if (i > 10) throw new RuntimeException("Error");
        }
    }

    private static void notifyAndRelease() {
        // to do something
        System.out.println("Notify the admin by email");

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            // ignored
        }

        System.out.println("To release all resource: socket,file,connection..");

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            // ignored
        }

        System.out.println("Success to notify and release source");
    }
}
