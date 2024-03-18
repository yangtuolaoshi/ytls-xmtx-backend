package love.ytlsnb.reward.feign;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/18 10:43
 */
@RestController
@RequestMapping("/reward")
public class RewardClient {
    @Autowired
    private RewardService rewardService;

    /**
     * 新增奖品
     * @param rewardInsertDTO
     * @return
     */
    @PostMapping
    public Result addReward(@RequestBody RewardDTO rewardInsertDTO){
        rewardService.add(rewardInsertDTO);
        return Result.ok();
    }

    /**
     * 分页查询奖品
     * @param rewardQueryDTO
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/page")
    public PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size){
        return rewardService.getPageByCondition(rewardQueryDTO, page, size);

    }

    /**
     * 修改奖品呢信息
     * @param rewardDTO
     * @return
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody RewardDTO rewardDTO){
        return Result.ok(rewardService.update(rewardDTO));
    }

    /**
     * 根据id删除奖品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<Boolean>deleteById(@PathVariable Long id){
        return Result.ok(rewardService.deleteById(id));

    }
}
