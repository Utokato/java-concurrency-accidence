package cn.utoakto.concurrency.chapter18;

/**
 * 接收 异步消息 的 主动方法
 *
 * @date 2019/5/7
 */
public interface ActiveObject {

    Result makeString(int count, char fillChar);

    void displayString(String text);
}
