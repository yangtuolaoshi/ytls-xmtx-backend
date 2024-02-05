package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:24
 */
@Data
@TableName("tb_building")
public class Building {
    /**
     * 学校建筑主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 建筑名
     */
    private String buildingName;
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
