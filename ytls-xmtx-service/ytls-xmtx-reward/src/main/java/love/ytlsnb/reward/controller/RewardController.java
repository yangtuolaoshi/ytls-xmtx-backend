package love.ytlsnb.reward.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ula
 * @date 2024/2/6 14:22
 */
@Slf4j
@RestController
@RequestMapping("reward")
public class RewardController {
    @Autowired
    private RewardService rewardService;
}
