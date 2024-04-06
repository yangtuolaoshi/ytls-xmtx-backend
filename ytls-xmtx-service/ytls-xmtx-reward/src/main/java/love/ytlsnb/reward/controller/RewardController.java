package love.ytlsnb.reward.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardPhotoDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.reward.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:22
 */
@Slf4j
@RestController
@RequestMapping("/reward")
public class RewardController {
}

//    @Autowired
//    private RewardService rewardService;
//
//
//    /**
//     * 新增奖品
//     * @param rewardInsertDTO
//     * @return
//     */
//    @PostMapping
//    public Result addReward(@RequestBody RewardDTO rewardInsertDTO){
//        log.info("新增奖品:{}",rewardInsertDTO);
//        rewardService.add(rewardInsertDTO);
//        return Result.ok();
//    }
//
//    /**
//     * 分页查询奖品
//     * @param rewardQueryDTO
//     * @param page
//     * @param size
//     * @return
//     */
//    @GetMapping("/page")
//    public PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size){
//        log.info("分页查询奖品:{}",rewardQueryDTO);
//        return rewardService.getPageByCondition(rewardQueryDTO, page, size);
//
//    }
//
//    /**
//     * 根据学校id查询奖品
//     * @param schoolId
//     * @return
//     */
//    @GetMapping("/{schoolId}")
//    public Result<List<Reward>> getPageBySchoolId(@PathVariable Long schoolId){
//        log.info("根据学校id查询奖品");
//        List<Reward> rewards= rewardService.selectBySchoolId(schoolId);
//        return Result.ok(rewards);
//    }
//
//    /**
//     * 修改奖品呢信息
//     * @param rewardDTO
//     * @return
//     */
//    @PutMapping("/update")
//    public Result<Boolean> update(@RequestBody RewardDTO rewardDTO){
//        log.info("修改奖品信息:{}",rewardDTO);
//        return Result.ok(rewardService.update(rewardDTO));
//    }
//
//    /**
//     * 根据id删除奖品
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/{id}")
//    public Result deleteWithPhotoById(@PathVariable Long id){
//        log.info("删除奖品所有信息:{}",id);
//        rewardService.deleteWithPhotoById(id);
//        return Result.ok();
//
//    }
//
//    @DeleteMapping("/photo/delete")
//    public Result deletePhoto(RewardPhotoDTO rewardPhotoDTO){
//        log.info("删除奖品图片id：{}",rewardPhotoDTO);
//        rewardService.deleteByPhoto(rewardPhotoDTO);
//        return Result.ok();
//    }
//
//
//}
