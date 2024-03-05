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
    /**
     * 用户手机号（非空）
     */
    private String phone;
    /**
     * 用户学院名（选填：但所填必须存在）
     */
    private String deptName;
    /**
     * 用户班级名（选填：但所填必须存在）
     */
    private String clazzName;
    /**
     * 用户学号（非空）
     */
    private String studentId;
    /**
     * 用户真实姓名（非空）
     */
    private String name;
    /**
     * 用户身份证号（非空）
     */
    private String idNumber;
}
