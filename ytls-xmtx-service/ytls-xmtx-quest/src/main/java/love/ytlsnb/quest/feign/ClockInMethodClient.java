package love.ytlsnb.quest.feign;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.po.ClockInMethod;
import love.ytlsnb.quest.service.ClockInMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 打卡方式远程调用提供
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@RestController
@RequestMapping("/clockInMethod")
@Slf4j
public class ClockInMethodClient {
    @Autowired
    private ClockInMethodService clockInMethodService;

    @GetMapping("/all")
    public Result<List<ClockInMethod>> getAll() {
        log.info("获取打卡方式...");
        return Result.ok(clockInMethodService.getAll());
    }
}
