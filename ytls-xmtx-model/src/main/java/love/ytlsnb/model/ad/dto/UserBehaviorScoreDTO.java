package love.ytlsnb.model.ad.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ula
 * @date 2024/3/23 14:31
 */
@Data
public class UserBehaviorScoreDTO {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 广告ID
     */
    private Long advertisementId;
    /**
     * 用户对于广告的操作分值
     */
    private BigDecimal score;
}
