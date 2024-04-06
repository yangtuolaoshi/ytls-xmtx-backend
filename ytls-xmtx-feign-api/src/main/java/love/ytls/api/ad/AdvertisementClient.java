package love.ytls.api.ad;

import love.ytlsnb.model.ad.dto.AdvertisementInsertDTO;
import love.ytlsnb.model.ad.dto.AdvertisementQueryDTO;
import love.ytlsnb.model.ad.po.Advertisement;
import love.ytlsnb.model.ad.vo.AdvertisementVO;
import love.ytlsnb.model.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/26 14:09
 */
@FeignClient(value = "ad-service", contextId = "advertisement")
public interface AdvertisementClient {
    @PostMapping("/api/ad")
    Result addAdvertisement(@RequestBody AdvertisementInsertDTO adInsertDTO);

    @DeleteMapping("/api/ad/{adId}")
    Result deleteAdvertisementById(@PathVariable Long adId);

    @PutMapping("/api/ad")
    Result updateAdvertisement(@RequestBody AdvertisementInsertDTO adUpdateDTO);

    @GetMapping("/api/ad/{adId}")
    Result<AdvertisementVO> getVOById(@PathVariable Long adId);

    @GetMapping("/api/ad/page")
    Result<List<AdvertisementVO>> getPageByConditions(@SpringQueryMap AdvertisementQueryDTO adQueryDTO);

    @GetMapping("/api/ad/{userId}/{size}")
    Result<List<Advertisement>> list2User(@PathVariable Long userId, @PathVariable Integer size);
}
