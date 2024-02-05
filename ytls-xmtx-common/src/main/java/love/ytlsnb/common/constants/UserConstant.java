package love.ytlsnb.common.constants;

/**
 * @author ula
 */
public class UserConstant {
    /**
     * 网关用来鉴别注册操作的常量
     */
    public static final String REGISTER_URL = "user/register";
    /**
     * 网关用来鉴别登录操作的常量
     */
    public static final String LOGIN_URL = "user/login";
    /**
     * 用户ID常量,用于数据库sql操作
     */
    public static final String USER_ID = "user_d";
    /**
     * 用户学号常量,用于数据库sql操作
     */
    public static final String STUDENT_ID = "student_id";
    /**
     * 用户学号最大长度
     */
    public static final Integer STUDENT_ID_MAX_LENGTH = 16;
    /**
     * 用户昵称常量
     */
    public static final String NICKNAME = "nickname";
    /**
     * 用户昵称最大长度
     */
    public static final Integer NICKNAME_MAX_LENGTH = 32;
    /**
     * 用户默认昵称前缀
     */
    public static final String DEFAULT_NICKNAME_PREFIX = "user_";
    /**
     * 默认生成用户名的随机字符串长度
     */
    public static final Integer DEFAULT_NICKNAME_LENGTH = 10;
    /**
     * 用户密码常量
     */
    public static final String PASSWORD = "password";
    /**
     * 用户密码最大长度
     */
    public static final Integer PASSWORD_MAX_LENGTH = 32;
    /**
     * 用户默认(未设置)性别
     */
    public static final Byte DEFAULT_GENDER = 0;
    /**
     * 男
     */
    public static final Byte MAN = 1;
    /**
     * 女
     */
    public static final Byte WOMAN = 2;
    /**
     * TODO：用户默认头像地址
     */
    public static final String DEFAULT_AVATAR = "";
    /**
     * 用户手机号常量
     */
    public static final String PHONE = "phone";
    /**
     * 用户手机号最大长度
     */
    public static final Integer PHONE_MAX_LENGTH = 11;
    /**
     * 用户初始积分
     */
    public static final Long INITIAL_POINT = 0L;
    /**
     * 用户认证字段常量(对应po中的字段)
     */
    public static final String IS_IDENTIFIED_JAVA = "identified";
    /**
     * 用户认证字段常量(对应数据库中的字段)
     */
    public static final String IS_IDENTIFIED = "is_identified";
    /**
     * 用户未认证
     */
    public static final Byte UNIDENTIFIED = 0;
    /**
     * 用户已认证
     */
    public static final Byte IDENTIFIED = 1;
    /**
     * 学校主键字段常量
     */
    public static final String SCHOOL_ID="school_id";
    /**
     * 学院主键字段常量
     */
    public static final String DEPT_ID="dept_id";
    /**
     * 班级主键字段常量
     */
    public static final String CLAZZ_ID="clazz_id";
    /**
     * 用户未删除
     */
    public static final Byte UNDELETED = 0;
    /**
     * 用户已删除
     */
    public static final Byte DELETED = 1;
}