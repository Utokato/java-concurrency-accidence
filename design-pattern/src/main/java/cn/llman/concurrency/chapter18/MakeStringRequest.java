package cn.llman.concurrency.chapter18;

/**
 * {@link ActiveObject#makeString(int, char)}
 *
 * @date 2019/5/7
 */
public class MakeStringRequest extends MethodRequest {

    private final int count;
    private final char fillChar;


    public MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.count = count;
        this.fillChar = fillChar;
    }

    @Override
    public void execute() {
        Result result = servant.makeString(count, fillChar);
        futureResult.setResult(result);
    }
}
