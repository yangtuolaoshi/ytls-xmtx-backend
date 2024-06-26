package love.ytlsnb.model.user.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * <p>
 * 用户注册数据传输对象
 */
@Data
@ToString
public class UserRegisterDTO {
    /**
     * 手机号，长度限制11位
     */
    private String phone;
    /**
     * 验证码，六位数字
     */
    private String code;
}
