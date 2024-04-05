package love.ytls.api.reward;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.model.reward.dto.ExchangeLogDTO;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/api/reward/{id}")
    Result<Reward> getByRewardId(@PathVariable Long id);

    @PostMapping("/api/exchange/add/exchange")
    Result addExchangeLog(ExchangeLogDTO exchangeLogDTO);

    @DeleteMapping("/api/reward/delWithPho/{id}")
    public Result deleteWithPhotoById(@PathVariable Long id);

    @DeleteMapping("/api/reward/photo/delete")
    public Result deletePhoto(RewardPhotoDTO rewardPhotoDTO);

    @PostMapping("/api/reward/uploadPhotos")
    public Result<String> uploadPhotos(MultipartFile file) ;

    @RequestMapping(method = RequestMethod.GET,value = "api/exchange/page")
    public PageResult<List<ExchangeLogVO>> getPageByCondition(
            @SpringQueryMap ExchangeLogQueryDTO exchangeLogQueryDTO,
            @RequestParam int page,
            @RequestParam int size);

    @GetMapping("/api/exchange/{id}")
    public Result<ExchangeLogVO> selectById(@PathVariable Long id);
}
