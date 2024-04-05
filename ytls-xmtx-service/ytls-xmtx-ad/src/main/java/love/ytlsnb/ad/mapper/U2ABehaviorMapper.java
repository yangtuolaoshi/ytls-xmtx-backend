package love.ytlsnb.ad.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.ad.dto.UserBehaviorScoreDTO;
import love.ytlsnb.model.ad.po.U2ABehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:54
 */
@Mapper
public interface U2ABehaviorMapper extends BaseMapper<U2ABehavior> {
    List<Long> listLikedAdIdsByUserId(Long userid);

    BigDecimal getAdSumScoreByAdId(Long adId);

    BigDecimal getScoreByUserIdAndAdId(Long userId, Long adId);

    List<UserBehaviorScoreDTO> listScoreByUserId(Long userId);

    List<UserBehaviorScoreDTO> listScoreByAdId(Long adId);
}
