package love.ytlsnb.ad.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.ad.job.AdvertisementJob;
import love.ytlsnb.ad.service.AdvertisementService;
import love.ytlsnb.model.ad.dto.AdvertisementInsertDTO;
import love.ytlsnb.model.ad.dto.AdvertisementQueryDTO;
import love.ytlsnb.model.ad.po.Advertisement;
import love.ytlsnb.model.ad.vo.AdvertisementVO;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:31
 */
@Slf4j
@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    private AdvertisementService adService;
    @Autowired
    private AdvertisementJob adJob;

    @GetMapping("test")
    public Result testJob() throws InterruptedException {
        adJob.calculateAdvertisementSimilarity();
        adJob.calculateRecommendationScore();
        return Result.ok();
    }

    @PostMapping
    public Result addAdvertisement(@RequestBody AdvertisementInsertDTO adInsertDTO) {
        log.info("新增广告:{}", adInsertDTO);
        adService.addAdvertisement(adInsertDTO);
        return Result.ok();
    }

    @DeleteMapping("/{adId}")
    public Result deleteAdvertisementById(@PathVariable Long adId) {
        log.info("根据ID删除广告:{}", adId);
        adService.deleteAdvertisementById(adId);
        return Result.ok();
    }

    /**
     * 根据数据传输对象修改广告，需要传递广告主键
     *
     * @param adUpdateDTO 数据传输对象
     * @return
     */
    @PutMapping
    public Result updateAdvertisement(@RequestBody AdvertisementInsertDTO adUpdateDTO) {
        log.info("修改广告:{}", adUpdateDTO);
        adService.updateAdvertisement(adUpdateDTO);
        return Result.ok();
    }

    @GetMapping("/{adId}")
    public Result<AdvertisementVO> getVOById(@PathVariable Long adId) {
        AdvertisementVO adVO = adService.getVOById(adId);
        return Result.ok(adVO);
    }

    @GetMapping("/page")
    public Result<List<AdvertisementVO>> getPageByConditions(AdvertisementQueryDTO adQueryDTO) {
        log.info("条件查询广告:{}", adQueryDTO);
        List<AdvertisementVO> adVOList = adService.getPageByConditions(adQueryDTO);
        return Result.ok(adVOList);
    }

    @GetMapping("/{userId}/{size}")
    public Result<List<Advertisement>> list2User(@PathVariable Long userId, @PathVariable Integer size) {
        log.info("对用户{} 推送广告size{}", userId, size);
        List<Advertisement> adList = adService.list2User(userId, size);
        return Result.ok(adList);
    }
}
