package love.ytlsnb.model.school.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/2/6 16:25
 */
@Data
@ToString
public class ColadminLoginDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
