package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:24
 */
@Data
@TableName("tb_location")
public class Location {
    /**
     * 学校建筑主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 关联的学校主键
     */
    private Long schoolId;
    /**
     * 建筑名
     */
    private String locationName;
    /**
     * 学校建筑的经度
     */
    private String longitude;
    /**
     * 学校建筑的维度
     */
    private String latitude;
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
