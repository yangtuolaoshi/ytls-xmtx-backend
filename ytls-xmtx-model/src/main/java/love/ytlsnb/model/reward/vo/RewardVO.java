package love.ytlsnb.model.reward.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import love.ytlsnb.model.reward.po.RewardPhoto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RewardVO {
    /**
     * 奖品主键
     */
    private Long id;
    /**
     * 奖品标题
     */
    private String title;
    /**
     * 奖品所需积分
     */
    private Integer cost;
    /**
     * 奖品库存
     */
    private Integer stock;
    /**
     * 奖品兑换量
     */
    private Integer exchangeSum;
    /**
     * 奖品状态
     */
    private Byte status;
    /**
     * 奖品所属学校主键
     */
    private Long schoolId;
    /**
     * 奖品图片
     */
    private List<RewardPhoto> rewardPhotos;

}
