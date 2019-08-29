package cn.llman.juc.atomic;

/**
 * @date 2019/5/16
 */
public class GetLockFailException extends Exception {

    public GetLockFailException() {
        super();
    }

    public GetLockFailException(String message) {
        super(message);
    }
}
