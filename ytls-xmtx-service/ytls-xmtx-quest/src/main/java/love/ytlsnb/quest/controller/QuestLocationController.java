package love.ytlsnb.quest.controller;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.vo.QuestLocationInfoVO;
import love.ytlsnb.quest.service.QuestLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务地点表现层
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@RestController
@RequestMapping("/questLocation")
public class QuestLocationController {
    @Autowired
    private QuestLocationService questLocationService;

    @GetMapping("/scheduleInfoPage/{scheduleId}")
    public Result<QuestLocationInfoVO> getScheduleInfoPageLocation(@PathVariable Long scheduleId) {
        return Result.ok(questLocationService.getQuestSchedulePageLocation(scheduleId));
    }
}
