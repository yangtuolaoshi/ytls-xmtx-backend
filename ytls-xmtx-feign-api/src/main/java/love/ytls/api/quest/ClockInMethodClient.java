package love.ytls.api.quest;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.po.ClockInMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 打卡方式远程调用
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@FeignClient(value = "quest-service", contextId = "clock-in-method")
public interface ClockInMethodClient {
    @GetMapping("/api/clockInMethod/all")
    Result<List<ClockInMethod>> getAll();
}
