package love.ytlsnb.model.reward.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/6 14:40
 */
@Data
@TableName("tb_exchange_log")
public class ExchangeLog {
    /**
     * 奖品主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 关联的奖品的主键
     */
    private Long rewardId;
    /**
     * 关联的用户的主键
     */
    private Long userId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
