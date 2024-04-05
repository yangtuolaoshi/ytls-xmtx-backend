package love.ytlsnb.ad.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.service.U2ABehaviorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ula
 * @date 2024/3/20 14:58
 */
@Slf4j
@RestController
@RequestMapping("/u2aBehavior")
public class U2ABehaviorController {
    @Autowired
    private U2ABehaviorService u2aBehaviorService;
}
