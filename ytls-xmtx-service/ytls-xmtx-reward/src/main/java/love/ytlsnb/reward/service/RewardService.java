package love.ytlsnb.reward.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:23
 */
public interface RewardService extends IService<Reward> {
    void add(RewardDTO rewardDTO);

    PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size);

    Boolean update(RewardDTO rewardDTO);

    Boolean deleteById(Long id);
}
