package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytls.api.ad.AdvertisementClient;
import love.ytlsnb.model.ad.dto.AdvertisementInsertDTO;
import love.ytlsnb.model.ad.dto.AdvertisementQueryDTO;
import love.ytlsnb.model.ad.po.Advertisement;
import love.ytlsnb.model.ad.vo.AdvertisementVO;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/26 14:10
 */
@Slf4j
@RestController
@RequestMapping("/ad")
public class AdvertisementController {
    @Autowired
    private AdvertisementClient adClient;

    @PostMapping
    public Result addAdvertisement(@RequestBody AdvertisementInsertDTO adInsertDTO) {
        log.info("新增广告:{}", adInsertDTO);
        return adClient.addAdvertisement(adInsertDTO);
    }

    @DeleteMapping("/{adId}")
    public Result deleteAdvertisementById(@PathVariable Long adId) {
        log.info("根据ID删除广告:{}", adId);
        return adClient.deleteAdvertisementById(adId);
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
        return adClient.updateAdvertisement(adUpdateDTO);
    }

    @GetMapping("/{adId}")
    public Result<AdvertisementVO> getVOById(@PathVariable Long adId) {
        log.info("根据ID查询广告:{}", adId);
        return adClient.getVOById(adId);
    }

    @GetMapping("/page")
    public Result<List<AdvertisementVO>> getPageByConditions(AdvertisementQueryDTO adQueryDTO) {
        log.info("条件查询广告:{}", adQueryDTO);
        return adClient.getPageByConditions(adQueryDTO);
    }

    @GetMapping("/{userId}/{size}")
    public Result<List<Advertisement>> list2User(@PathVariable Long userId, @PathVariable Integer size) {
        log.info("对用户{} 推送广告size{}", userId, size);
        return adClient.list2User(userId, size);
    }
}
