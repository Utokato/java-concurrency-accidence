package cn.utoakto.concurrency.chapter15;

/**
 * @date 2019/5/5
 */
public class Message {

    private final String value;

    public Message(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
