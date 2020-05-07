package cn.utoakto.concurrency.chapter7;

/**
 * @date 2019/5/2
 */
public class UsePersonThread extends Thread {


    private Person person;

    public UsePersonThread(Person person) {
        this.person = person;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName() + " print " + person.toString());
        }
    }
}
