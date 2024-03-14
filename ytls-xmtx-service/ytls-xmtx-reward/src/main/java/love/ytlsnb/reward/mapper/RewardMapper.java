package love.ytlsnb.reward.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:26
 */
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {
    List<RewardVO> getPageByCondition(RewardQueryDTO rewardQueryDTO, int i, int size);
}
