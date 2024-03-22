package love.ytlsnb.common.constants;

/**
 * @author ula
 * @date 2024/2/6 16:37
 */
public class SchoolConstant {
    //-------------------------Coladmin-------------------------
    /**
     * 拦截器用来鉴别用户操作的常量
     */
    public static final String COLADMIN_ALL_URL = "/coladmin/**";
    /**
     * 网关和拦截器用来鉴别注册操作的常量
     */
    public static final String COLADMIN_REGISTER_URL = "/coladmin/register";
    /**
     * 网关和拦截器用来鉴别登录操作的常量
     */
    public static final String COLADMIN_LOGIN_URL = "/coladmin/login";
    /**
     * 网关和拦截器用来鉴别重置密码操作的常量
     */
    public static final String COLADMIN_REPASSWORD_URL = "/coladmin/repassword";
    /**
     * 网关和拦截器用来鉴别发送验证码操作的常量
     */
    public static final String COLADMIN_GETCODE_URL = "/coladmin/code";
    /**
     * 账号ID字段常量：jwt信息存储
     */
    public static final String COLADMIN_ID = "coladmin_id";
    /**
     * 账号状态字段常量
     */
    public static final Byte DISABLED = 0;
    /**
     * 账号状态字段常量
     */
    public static final Byte ENABLED = 1;
    /**
     * 学校ID字段常量：jwt信息存储
     */
    public static final String INSENSITIVE_PASSWORD = "********";
    //-------------------------Location-------------------------
    //-------------------------School-------------------------
    /**
     * 拦截器用来鉴别学校操作的常量
     */
    public static final String SCHOOL_ALL_URL = "/school/**";
    //-------------------------Dept-------------------------
    public static final Byte IS_DELETED = 1;
    //-------------------------Clazz-------------------------
    //-------------------------Quest-------------------------
    /**
     * 拦截器用来鉴别学校操作的常量
     */
    public static final String QUEST_ALL_URL = "/quest/**";
    //-------------------------Schedule-------------------------
    /**
     * 拦截器用来鉴别学校操作的常量
     */
    public static final String SCHEDULE_ALL_URL = "/schedule/**";
    public static final String USER_ID = "user_id";

}
