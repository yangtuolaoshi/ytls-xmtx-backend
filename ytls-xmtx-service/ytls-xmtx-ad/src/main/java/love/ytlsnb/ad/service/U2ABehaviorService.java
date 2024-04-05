package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.dto.UserBehaviorScoreDTO;
import love.ytlsnb.model.ad.po.U2ABehavior;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:55
 */
public interface U2ABehaviorService extends IService<U2ABehavior> {
    List<Long> listLikedAdIdsByUserId(Long userId);

    BigDecimal getAdSumScoreByAdId(Long adId);

    BigDecimal getScoreByUserIdAndAdId(Long userId, Long adId);

    List<UserBehaviorScoreDTO> listScoreByUserId(Long userId);

    List<UserBehaviorScoreDTO> listScoreByAdId(Long adId);
}
