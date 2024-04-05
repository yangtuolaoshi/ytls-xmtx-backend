package love.ytlsnb.model.common;

/**
 * @author ula
 * @date 2024/3/13 19:56
 */
public enum LoginType {
    /**
     * 身份证号+密码登录
     */
    ACCOUNT,
    /**
     * 手机号验证码登录
     */
    PHONE,
    /**
     * 微信授权登录
     */
    WX,
    /**
     * 华为账号授权登录
     */
    HW;
}
