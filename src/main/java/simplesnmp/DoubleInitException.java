package simplesnmp;

/**
 * Created by yhy on 17-5-26.
 *
 * 多次初始化 SimpleSNMP 模块。因为每次初始化都要监听端口。
 *
 */
public class DoubleInitException extends SimpleSNMPException{
    DoubleInitException(String message) {
        super(message);
    }
}
