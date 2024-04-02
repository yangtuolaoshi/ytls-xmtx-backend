package love.ytlsnb.model.reward.dto;

import lombok.Data;
import lombok.ToString;
import love.ytlsnb.model.reward.po.RewardPhoto;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class RewardDTO {

    /**
     * 奖品主键
     */
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
    /**
     * 奖品图片
     */
    private List<RewardPhoto> rewardPhotos = new ArrayList<>();



}
