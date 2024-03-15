package love.ytlsnb.common.constants;

import java.util.List;

/**
 * @author ula
 */
public class UserConstant {
    /**
     * 拦截器用来鉴别用户操作的常量
     */
    public static final String USER_ALL_URL = "/user/**";
    /**
     * 网关和拦截器用来鉴别注册操作的常量
     */
    public static final String USER_REGISTER_URL = "/user/register";
    /**
     * 网关和拦截器用来鉴别登录操作的常量
     */
    public static final String USER_LOGIN_URL = "/user/login";
    /**
     * 网关和拦截器用来鉴别重置密码操作的常量
     */
    public static final String USER_REPASSWORD_URL = "/user/repassword";
    /**
     * 网关和拦截器用来鉴别发送验证码操作的常量
     */
    public static final String USER_GETCODE_URL = "/user/code";
    //-------------------------User-------------------------
    /**
     * 用户ID常量,用于数据库sql操作
     */
    public static final String USER_ID = "user_id";
    /**
     * 用户学号最大长度
     */
    public static final Integer STUDENT_ID_MAX_LENGTH = 16;
    public static final String NICKNAME="nickname";
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
    public static final Integer DEFAULT_NICKNAME_LENGTH = 6;
    /**
     * 用户密码脱敏后常量
     */
    public static final String INSENSITIVE_PASSWORD = "********";
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
     * 用户未删除
     */
    public static final Byte UNDELETED = 0;
    /**
     * 用户已删除
     */
    public static final Byte DELETED = 1;
    //-------------------------UserInfo-------------------------
    public static final String NAME="name";
}