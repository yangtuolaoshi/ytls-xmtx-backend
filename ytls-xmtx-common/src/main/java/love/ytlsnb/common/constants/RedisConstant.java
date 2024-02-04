package love.ytlsnb.common.constants;

/**
 * @author ula
 * <p>
 * 所有的前缀都以‘:’结尾
 */
public class RedisConstant {
    public static final String SEPARATOR = ":";
    public static final String LOCK_PREFIX = "lock:";
    public static final String USER_REGISTER_LOCK_PREFIX = "lock:user:register:";
    public static final String USER_LOGIN_LOCK_PREFIX = "lock:user:login:";
    public static final String USER_SIGN_LOCK_PREFIX = "lock:user:sign:";
    public static final String USER_SIGN_PERMOUTH_PATTERN = "yyyy:MM:";
    public static final String USER_SIGN_PREFIX = "user:sign:";
    public static final String USER_LOGIN_PREFIX = "user:login:";
    public static final String SCHOOL_RANKING_LIST_TOTAL_PREFIX = "school:ranking-list:total:";
}
