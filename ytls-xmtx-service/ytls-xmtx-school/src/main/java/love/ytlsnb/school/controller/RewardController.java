package love.ytlsnb.school.controller;

import love.ytls.api.reward.RewardClient;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.*;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/4/5 15:59
 */
@RestController
@RequestMapping("/reward")
public class RewardController {
    @Autowired
    private RewardClient rewardClient;


    /**
     * 新增奖品
     * @param rewardInsertDTO
     * @return
     */
    @PostMapping
    public Result addReward(@RequestBody RewardDTO rewardInsertDTO){
        rewardClient.addReward(rewardInsertDTO);
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

        return rewardClient.getPageByCondition(rewardQueryDTO, page, size);

    }

    /**
     * 根据学校id查询奖品
     * @param schoolId
     * @return
     */
    @GetMapping("/{schoolId}")
    public Result<List<Reward>> getPageBySchoolId(@PathVariable Long schoolId){
        Result<List<Reward>> pageBySchoolId = rewardClient.getPageBySchoolId(schoolId);
        return pageBySchoolId;
    }

    /**
     * 修改奖品呢信息
     * @param rewardDTO
     * @return
     */
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody RewardDTO rewardDTO){

        return rewardClient.update(rewardDTO);
    }

    /**
     * 根据id删除奖品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteWithPhotoById(@PathVariable Long id){

        rewardClient.deleteById(id);
        return Result.ok();

    }

    @DeleteMapping("/photo/delete")
    public Result deletePhoto(RewardPhotoDTO rewardPhotoDTO){
        rewardClient.deletePhoto(rewardPhotoDTO);
        return Result.ok();
    }
    @PostMapping("/add")
    public Result addExchangeLog(ExchangeLogDTO exchangeLogDTO){

        rewardClient.addExchangeLog(exchangeLogDTO);
        return Result.ok();
    }

    @GetMapping("/page")
    public PageResult<List<ExchangeLogVO>> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size){
        return rewardClient.getPageByCondition(exchangeLogQueryDTO,page,size);

    }

    @GetMapping("/{id}")
    public Result<ExchangeLogVO> selectById(@PathVariable Long id){
    return rewardClient.selectById(id);

    }

}
