package cn.llman.concurrency.chapter18;

/**
 * @date 2019/5/7
 */
public class ActiveObjectClient {
    public static void main(String[] args) {


        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        new MakerClientThread("Alisa", activeObject).start();
        new MakerClientThread("Bob", activeObject).start();
        new DisplayClientThread("Catalina", activeObject).start();

    }
}
