package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytls.api.ad.AdvertisementClient;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("test")
    public Result test(){
        log.info("test");
        adClient.test();
        return Result.ok();
    }
}
