package love.ytlsnb.common.constants;

/**
 * @author ula
 */
public class UserConstant {
    public static final String REGISTER_URL = "user/register";
    public static final String LOGIN_URL = "user/login";
    public static final String USER_ID = "user_id";
    public static final String STUDENT_ID = "student_id";
    public static final Integer STUDENT_ID_MAX_LENGTH = 16;
    public static final String NICKNAME = "nickname";
    public static final Integer NICKNAME_MAX_LENGTH = 32;
    public static final String DEFAULT_NICKNAME_PREFIX = "user_";
    // 默认生成用户名的随机字符串长度
    public static final Integer DEFAULT_NICKNAME_LENGTH = 10;
    public static final String PASSWORD = "password";
    public static final Integer PASSWORD_MAX_LENGTH = 32;
    public static final Byte DEFAULT_GENDER = 0;
    public static final String DEFAULT_AVATAR = "";
    public static final String PHONE = "phone";
    public static final Integer PHONE_MAX_LENGTH = 11;
    public static final Long DEFAULT_POINT = 0L;
    public static final Byte UNIDENTIFIED = 0;
    public static final Byte IDENTIFIED = 1;
    public static final Byte UNDELETED = 0;
    public static final Byte DELETED = 1;
}
