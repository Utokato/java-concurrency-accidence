package cn.utoakto.concurrency.chapter5;

/**
 * @date 2019/5/2
 */
public class Client {

    public static void main(String[] args) {

        Gate gate = new Gate();
        User bob = new User("Bob", "Beijing", gate);
        User selina = new User("Selina", "Shanghai", gate);
        User given = new User("Given", "Guangzhou", gate);

        bob.start();
        selina.start();
        given.start();
    }
}
