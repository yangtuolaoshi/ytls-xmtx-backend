package love.ytlsnb.reward.controller;

import lombok.extern.slf4j.Slf4j;
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
 * @author ula
 * @date 2024/2/6 14:22
 */
@Slf4j
@RestController
@RequestMapping("/reward")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    @PostMapping
    public Result addReward(@RequestBody RewardDTO rewardInsertDTO){
        log.info("新增奖品:{}",rewardInsertDTO);
        rewardService.add(rewardInsertDTO);
        return Result.ok();
    }

    @GetMapping("/page")
    public PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size){
        log.info("分页查询奖品:{}",rewardQueryDTO);
        return rewardService.getPageByCondition(rewardQueryDTO, page, size);

    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody RewardDTO rewardDTO){
        log.info("修改奖品信息:{}",rewardDTO);
        return Result.ok(rewardService.update(rewardDTO));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean>deleteById(@PathVariable Long id){
        log.info("删除奖品信息:{}",id);
        return Result.ok(rewardService.deleteById(id));

    }
}
