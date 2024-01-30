package love.ytlsnb.model.user.pojo.dto;

import lombok.Data;

/**
 * @author ula
 *
 * 用户登录数据传输对象
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
}
