package cn.utoakto.concurrency.chapter9;

/**
 * @date 2019/5/3
 */
public class Request {

    private final String value;

    public Request(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
