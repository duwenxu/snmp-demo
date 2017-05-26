package simplesnmp;

/**
 * Created by yhy on 17-5-26.
 *
 * SimpleSNMP 模块的基类
 *
 */
public class SimpleSNMPException extends RuntimeException {
    SimpleSNMPException(String message ){
        super(message);
    }
}
