package love.ytlsnb.model.reward.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RewardQueryDTO {
    /**
     * 奖品标题
     */
    private String title;
    /**
     * 奖品所需积分
     */
    private Integer cost;
    /**
     * 奖品状态
     */
    private Byte status;
}
