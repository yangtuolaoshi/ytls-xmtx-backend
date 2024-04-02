package love.ytlsnb.school.controller;

import love.ytls.api.quest.ClockInMethodClient;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.po.ClockInMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 打卡方式表现层
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@RestController
@RequestMapping("/clockInMethod")
public class ClockInMethodController {
    @Autowired
    private ClockInMethodClient clockInMethodClient;

    @GetMapping("/all")
    public Result<List<ClockInMethod>> getAll() {
        return clockInMethodClient.getAll();
    }
}
