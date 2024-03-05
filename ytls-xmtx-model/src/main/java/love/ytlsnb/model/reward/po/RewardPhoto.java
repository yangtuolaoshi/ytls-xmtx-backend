package love.ytlsnb.model.reward.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/6 14:37
 */
@Data
@TableName("tb_reward_photo")
public class RewardPhoto {
    /**
     * 奖品照片主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 关联的奖品主键
     */
    private Long rewardId;
    /**
     * 奖品照片存储地址
     */
    private String photo;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
