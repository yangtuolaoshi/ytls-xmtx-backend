package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:25
 */
@Data
@TableName("tb_clazz")
public class Clazz {
    /**
     * 班级主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 班级所属学院主键
     */
    private Long deptId;
    /**
     * 班级名
     */
    private String clazzName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
