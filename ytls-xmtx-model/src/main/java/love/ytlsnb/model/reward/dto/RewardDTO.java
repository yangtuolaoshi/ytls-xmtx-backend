package love.ytlsnb.model.reward.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RewardDTO {

    private Long id;
    /**
     * 奖品标题
     */
    private String title;
    /**
     * 奖品子标题
     */
    private String subtitle;
    /**
     * 奖品描述
     */
    private String description;
    /**
     * 奖品兑换方式
     */
    private String exchangeMethod;
    /**
     * 奖品所需积分
     */
    private Integer cost;
    /**
     * 奖品库存
     */
    private Integer stock;
    /**
     * 奖品状态
     */
    private Byte status;



}
