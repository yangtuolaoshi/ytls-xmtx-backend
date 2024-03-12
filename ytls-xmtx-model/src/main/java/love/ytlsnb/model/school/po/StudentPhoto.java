package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:26
 */
@Data
@TableName("tb_student_photo")
public class StudentPhoto {
    /**
     * 学生照片主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 所属学生学号
     */
    private String studentId;
    /**
     * 照片地址
     */
    private String photo;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
