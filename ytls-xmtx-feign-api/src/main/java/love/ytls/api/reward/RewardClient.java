package love.ytls.api.reward;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/18 10:29
 */
@FeignClient("reward-service")
public interface RewardClient {

    @PostMapping("/api/reward")
    Result addReward(@RequestBody RewardDTO rewardInsertDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/api/reward/page")
    PageResult<List<RewardVO>> getPageByCondition(
            @SpringQueryMap RewardQueryDTO RewardQueryDTO,
            @RequestParam int page,
            @RequestParam int size
    );

    @PutMapping("/api/reward/update")
    Result<Boolean> update(@RequestBody RewardDTO rewardDTO);

    @DeleteMapping("/api/reward/{id}")
    Result<Boolean>deleteById(@PathVariable Long id);

    @GetMapping("/api/reward/{schoolId}")
    Result<List<Reward>> getPageBySchoolId(@PathVariable Long schoolId);
}
