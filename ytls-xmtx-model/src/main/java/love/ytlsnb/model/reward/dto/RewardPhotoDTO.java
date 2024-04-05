package love.ytlsnb.model.reward.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author QiaoQiao
 * @date 2024/3/20 16:28
 */
@Data
@ToString
public class RewardPhotoDTO {
    /**
     * 奖品照片主键
     */
    private Long id;
    /**
     * 关联的奖品主键
     */
    private Long rewardId;
    /**
     * 奖品照片存储地址
     */
    private String photo;

}
