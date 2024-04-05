package love.ytlsnb.ad.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.service.AdvertisementSimilarityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ula
 * @date 2024/3/20 14:42
 */
@Slf4j
@RestController
@RequestMapping("/adSimilarity")
public class AdvertisementSimilarityController {
    @Autowired
    private AdvertisementSimilarityService advertisementSimilarityService;
}
