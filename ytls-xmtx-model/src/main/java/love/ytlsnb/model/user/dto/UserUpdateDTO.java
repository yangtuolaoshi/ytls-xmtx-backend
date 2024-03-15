package love.ytlsnb.model.user.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/3/10 11:40
 */
@Data
@ToString
public class UserUpdateDTO {
    /**
     * 用户待修改的昵称
     */
    private String nickname;
    /**
     * 用户待修改的签名
     */
    private String sign;
    /**
     * 用户待修改的手机号
     */
    private String phone;
    /**
     * 用户待修改的手机号的验证码
     */
    private String code;
}

