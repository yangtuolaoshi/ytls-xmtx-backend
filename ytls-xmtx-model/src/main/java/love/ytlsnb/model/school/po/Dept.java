package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:25
 */
@Data
@TableName("tb_dept")
public class Dept  implements Serializable {
    /**
     * 学院主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 学院所属学校主键
     */
    private Long schoolId;
    /**
     * 学院名
     */
    private String deptName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
