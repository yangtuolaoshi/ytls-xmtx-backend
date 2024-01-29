package love.ytlsnb.common.exception;

/**
 * @author ula
 */
public class UserLoginException extends BusinessException{
    public UserLoginException(int code, String msg) {
        super(code, msg);
    }
}
