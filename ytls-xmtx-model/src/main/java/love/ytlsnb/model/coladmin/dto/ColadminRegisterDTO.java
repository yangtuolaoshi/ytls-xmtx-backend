package love.ytlsnb.model.coladmin.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/2/7 14:29
 */
@Data
@ToString
public class ColadminRegisterDTO {
    /**
     * 管理员账户用户名（唯一）
     */
    private String username;
    /**
     * 管理员账户密码
     */
    private String password;
    /**
     * 需要生成的账号权限
     */
    private Byte privilege;
    /**
     * 当前账号对应的学校ID
     */
    private Long schoolId;
}
