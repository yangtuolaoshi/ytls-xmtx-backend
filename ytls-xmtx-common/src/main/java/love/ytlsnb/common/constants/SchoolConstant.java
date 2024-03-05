package love.ytlsnb.common.constants;

/**
 * @author ula
 * @date 2024/2/6 16:37
 */
public class SchoolConstant {
    //-------------------------Coladmin-------------------------
    /**
     * 主键字段
     */
    public static final String ID = "id";
    /**
     * 账号ID字段常量：jwt信息存储
     */
    public static final String COLADMIN_ID = "coladmin_id";
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
    public static final String INSENSITIVE_PASSWORD = "********";
    /**
     * 网关用来鉴别登录操作的字符串常量
     */
    public static final String LOGIN_URL = "coladmin/login";
    /**
     * 网关用来鉴别注册操作的字符串常量
     */
    public static final String REGISTER_URL = "coladmin/register";
    //-------------------------Location-------------------------
    /**
     * 学校地点ID
     */
    public static final String LOCATION_ID = "location_id";
    //-------------------------School-------------------------
    /**
     * 学校ID字段常量
     */
    public static final String SCHOOL_ID = "school_id";
    /**
     * 学校名称字段常量
     */
    public static final String SCHOOL_NAME = "school_name";
    //-------------------------Dept-------------------------
    /**
     * 学院名称字段常量
     */
    public static final String DEPT_NAME = "dept_name";
    //-------------------------Clazz-------------------------
    /**
     * 班级名称字段常量
     */
    public static final String CLAZZ_NAME = "clazz_name";
    public static final String USER_ID = "user_id";
}
