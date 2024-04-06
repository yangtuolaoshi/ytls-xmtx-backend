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
@TableName("tb_location_photo")
public class LocationPhoto {
    /**
     * 学校建筑图片主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 学校建筑主键
     */
    private Long locationId;
    /**
     * 相关照片地址
     */
    private String photo;
    /**
     * 是否是默认照片
     */
    private Byte cover;
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
