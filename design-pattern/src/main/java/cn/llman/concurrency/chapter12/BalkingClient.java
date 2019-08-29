package cn.llman.concurrency.chapter12;

/**
 * @date 2019/5/3
 */
public class BalkingClient {
    public static void main(String[] args) {
        String fileName = "F:\\Intellij_IDEA_Projects\\Accidence\\java-concurrency-accidence\\design-pattern\\src\\main\\java\\cn\\llman\\concurrency\\chapter12\\balking.txt";
        BalkingData balkingData = new BalkingData(fileName, "===BEGIN===");
        new CustomerThread(balkingData).start();
        new WaiterThread(balkingData).start();
    }
}
