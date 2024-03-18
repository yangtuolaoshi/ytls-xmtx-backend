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
     * 学号：当且仅当学校ID存在时生效
     */
    private String studentId;
    /**
     * 性别：0未设置 1男 2女
     */
    private Byte gender;
    /**
     * 手机号：最大长度11位
     */
    private String phone;
    /**
     * 是否认证: 0 未认证 1 已认证
     */
    private Byte identified;
    /**
     * 用户学校ID
     */
    private Long schoolId;
    /**
     * 用户学院ID：当且仅当学校ID存在时生效
     */
    private Long deptId;
    /**
     * 用户班级ID：当且仅当学校ID与学院ID存在时生效
     */
    private Long clazzId;
    /**
     * 用户真实姓名
     */
    private String name;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer pageSize;
}
