package love.ytlsnb.model.user.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/3/10 9:34
 */
@Data
@ToString
public class UserUpdatePasswordDTO {
    /**
     * 用户的手机号
     */
    private String phone;
    /**
     * 验证码（六位数字）
     */
    private String code;
    /**
     * 用户重置的新密码
     */
    private String password;
    /**
     * 用户确认密码
     */
    private String repassword;
}
