package cn.utoakto.concurrency.chapter18;

/**
 * {@link ActiveObject#displayString(String)}
 *
 * @date 2019/5/7
 */
public class DisplayStringRequest extends MethodRequest {

    private final String text;

    public DisplayStringRequest(Servant servant, final String text) {
        super(servant, null);
        this.text = text;
    }

    @Override
    public void execute() {
        servant.displayString(text);
    }
}
