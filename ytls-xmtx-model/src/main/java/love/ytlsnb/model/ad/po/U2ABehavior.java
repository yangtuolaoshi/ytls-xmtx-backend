package love.ytlsnb.model.ad.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/3/20 14:23
 */
@Data
@ToString
@TableName("tb_u2a_behavior")
public class U2ABehavior {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 广告ID
     */
    private Long advertisementId;
    /**
     * 用户的操作类型
     */
    private Byte type;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
