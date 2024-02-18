package love.ytlsnb.model.user.dto;

import lombok.Data;

/**
 * 使用Excel批量插入用户的数据传输对象
 *
 * @author ula
 * @date 2024/2/1 11:50
 */
@Data
public class UserInsertBatchDTO {
    private String className;
    private String studentId;
    private String name;
    private String phone;
}
