package love.ytlsnb.reward.feign;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.*;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.ExchangeLogVO;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.reward.service.ExchangeLogService;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ExchangeLogService exchangeLogService;

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

//    /**
//     * 根据id删除奖品
//     * @param id
//     * @return
//     */
//    @DeleteMapping("delWithPho/{id}")
//    public Result deleteById(@PathVariable Long id){
//
//        rewardService.deleteWithPhotoById(id);
//        return Result.ok();
//
//    }

    @GetMapping("/{schoolId}")
    public Result<List<Reward>> getPageBySchoolId(@PathVariable Long schoolId){
        List<Reward> rewards= rewardService.selectBySchoolId(schoolId);
        return Result.ok(rewards);
    }

    @GetMapping("/{id}")
    public Result<Reward> getByRewardId(@PathVariable Long id){
        Reward reward = rewardService.getById(id);
        return Result.ok(reward);
    }

    @PostMapping("/add/exchange")
    public Result addExchangeLog(ExchangeLogDTO exchangeLogDTO){
        exchangeLogService.addExchangeLog(exchangeLogDTO);
        return Result.ok();
    }
    @DeleteMapping("delWithPho/{id}")
    public Result deleteWithPhotoById(@PathVariable Long id){
        rewardService.deleteWithPhotoById(id);
        return Result.ok();

    }

    @DeleteMapping("/photo/delete")
    public Result deletePhoto(RewardPhotoDTO rewardPhotoDTO){
        rewardService.deleteByPhoto(rewardPhotoDTO);
        return Result.ok();
    }

    @PostMapping("/uploadPhotos")
    public Result<String> uploadPhotos(MultipartFile file) {
        return Result.ok(rewardService.upload(file));
    }


    @GetMapping("exchangeLog/page")
    public PageResult<List<ExchangeLogVO>> getPageByCondition(ExchangeLogQueryDTO exchangeLogQueryDTO, int page, int size){
        return exchangeLogService.getPageByCondition(exchangeLogQueryDTO,page,size);
    }

    @GetMapping("select/{id}")
    public Result<ExchangeLogVO> selectById(@PathVariable Long id){
        return Result.ok(exchangeLogService.selectById(id));
    }
}
