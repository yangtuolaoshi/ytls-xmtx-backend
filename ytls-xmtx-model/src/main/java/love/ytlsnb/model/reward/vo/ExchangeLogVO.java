package love.ytlsnb.model.reward.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author QiaoQiao
 * @date 2024/3/22 8:11
 */
@Data
@ToString
public class ExchangeLogVO {
    /**
     * 奖品主键
     */
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
    private LocalDateTime createTime;
}
