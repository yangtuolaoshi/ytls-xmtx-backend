package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/1/30 20:24
 */
@Data
@TableName("tb_school")
public class School {
    /**
     * 学校主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    Long id;
    /**
     * 学校名
     */
    String schoolName;
    /**
     * 学校标识
     */
    String icon;
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
