package love.ytlsnb.model.user.dto;

import lombok.Data;

/**
 * 用户登录数据传输对象，四个字段中最多含有两个有效字段
 *
 * @author ula
 */
@Data
public class UserLoginDTO {
    /**
     * 登录的账号：学号/手机号 长度限制16位
     */
    private String account;
    /**
     * 密码，长度限制32位
     */
    private String password;
    /**
     * 手机号登录
     */
    private String phone;
    /**
     * 验证码
     */
    private String code;
}
