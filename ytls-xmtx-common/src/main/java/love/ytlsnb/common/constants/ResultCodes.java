package love.ytlsnb.common.constants;

/**
 * 表现层返回状态码
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
public class ResultCodes {
    /**
     * 用户未登录 / 权限验证不通过
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 系统内部异常：一般用于未知异常
     */
    public static final int SERVER_ERROR = 500;
}
