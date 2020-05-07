package cn.utoakto.concurrency.chapter7;

/**
 * 不可变对象
 *
 * @date 2019/5/2
 */
final public class Person {

    private final String name;
    private final String address;


    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
