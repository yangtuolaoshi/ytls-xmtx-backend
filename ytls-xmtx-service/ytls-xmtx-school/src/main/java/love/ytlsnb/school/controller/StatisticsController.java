package love.ytlsnb.school.controller;

import love.ytlsnb.model.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据中心表现层
 *
 * @author 金泓宇
 * @date 2024/4/3
 */
@RestController
@RequestMapping("/statistic")
public class StatisticsController {
    /**
     * 获取已认证用户的比率
     * @return 百分比
     */
    public Result<Double> getIdentifiedRatio() {
        return Result.ok();
    }
}
