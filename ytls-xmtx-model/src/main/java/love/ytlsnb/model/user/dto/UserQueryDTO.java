package love.ytlsnb.model.user.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/3 15:57
 */
@Data
@ToString
public class UserQueryDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 学号
     */
    private String studentId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别：0未设置 1男 2女
     */
    private Byte gender;
    /**
     * 手机号：最大长度11位
     */
    private String phone;
    /**
     * 积分
     */
    private Long point;
    /**
     * 是否认证
     */
    private Byte identified;
    /**
     * 用户学校ID
     */
    private Long schoolId;
    /**
     * 用户部门ID
     */
    private Long deptId;
    /**
     * 用户班级ID
     */
    private Long classId;
}
