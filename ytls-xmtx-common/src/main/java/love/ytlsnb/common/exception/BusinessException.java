package love.ytlsnb.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义业务异常
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    /**
     * 返回状态码
     */
    private int code;

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
