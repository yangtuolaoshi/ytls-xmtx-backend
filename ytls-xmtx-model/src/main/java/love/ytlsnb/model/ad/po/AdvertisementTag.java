package love.ytlsnb.model.ad.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/3/23 19:26
 */
@Data
@TableName("tb_advertisement_tag")
public class AdvertisementTag {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 广告ID
     */
    private Long advertisementId;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
