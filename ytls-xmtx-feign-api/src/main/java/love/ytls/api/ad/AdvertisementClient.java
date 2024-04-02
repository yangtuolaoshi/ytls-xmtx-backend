package love.ytls.api.ad;

import love.ytlsnb.model.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author ula
 * @date 2024/3/26 14:09
 */
@FeignClient(value = "ad-service", contextId = "advertisement")
public interface AdvertisementClient {
    @GetMapping("/api/ad/test")
    Result test();
}
