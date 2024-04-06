package love.ytlsnb.common.constants;

/**
 * 表现层返回状态码
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
public class ResultCodes {
    /**
     * 请求成功。一般用于GET与POST请求
     */
    public static final int OK = 200;
    /**
     * 客户端请求的语法错误，服务器无法理解
     */
    public static final int BAD_REQUEST = 400;
    /**
     * 用户未登录 / 权限验证不通过
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    public static final int FORBIDDEN = 403;
    /**
     * 请求失败，请求所希望得到的资源未被在服务器上发现
     */
    public static final int NOT_FOUND = 404;
    /**
     * 服务器无法根据客户端请求的内容特性完成请求
     */
    public static final int NOT_ACCEPTABLE = 406;
    /**
     * 请求格式正确，但是由于含有语义错误，无法响应。
     */
    public static final int UNPROCESSABLE_ENTITY = 422;
    /**
     * 系统内部异常：一般用于未知异常
     */
    public static final int SERVER_ERROR = 500;
}
