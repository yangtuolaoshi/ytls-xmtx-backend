package love.ytlsnb.model.user.pojo.dto;

import lombok.Data;

/**
 * @author ula
 *
 * 用户注册数据传输对象
 */
@Data
public class UserRegisterDTO {
    /**
     * 学号
     */
    private String studentId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码，长度限制32位
     */
    private String password;
    /**
     * 手机号，长度限制11位
     */
    private String phone;
    /**
     * 验证码，六位数字
     */
    private String code;
}
