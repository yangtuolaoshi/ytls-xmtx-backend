package love.ytlsnb.common.constants;

/**
 * @author ula
 * @date 2024/2/6 16:37
 */
public class SchoolConstant {
    /**
     * 主键字段
     */
    public static final String ID = "id";
    /**
     * 账号ID字段常量：jwt信息存储
     */
    public static final String ADMIN_ID = "admin_id";
    /**
     * 学校管理人员账号用户名常量
     */
    public static final String USERNAME = "username";
    /**
     * 账号状态字段常量
     */
    public static final String STATUS = "status";
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
    public static final String SCHOOL_ID = "school_id";
    /**
     * 学校ID字段常量：jwt信息存储
     */
    public static final String INSENSITIVE_PASSWORD = "********";
    /**
     * 网关用来鉴别登录操作的字符串常量
     */
    public static final String LOGIN_URL = "school/login";
    /**
     * 网关用来鉴别注册操作的字符串常量
     */
    public static final String REGISTER_URL = "school/register";
    public static final String ADMIN_REGISTER_LOCK_PREFIX = "lock:admin:register:";
}
