package love.ytlsnb.reward.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.reward.mapper.RewardMapper;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/2/6 14:25
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {
    @Autowired
    private RewardMapper rewardMapper;
}
