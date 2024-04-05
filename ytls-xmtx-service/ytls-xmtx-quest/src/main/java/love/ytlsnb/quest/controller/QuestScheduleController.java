package love.ytlsnb.quest.controller;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.vo.QuestCardVO;
import love.ytlsnb.model.quest.vo.QuestScheduleCompletionVO;
import love.ytlsnb.model.quest.vo.QuestScheduleMapPoint;
import love.ytlsnb.model.quest.vo.QuestSchedulePageInfoVO;
import love.ytlsnb.quest.service.QuestScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * 任务进度表现层
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@RestController
@RequestMapping("/schedule")
public class QuestScheduleController {
    @Autowired
    private QuestScheduleService questScheduleService;

    /**
     * 任务详情页-进度信息
     * @param questId 任务ID
     * @return 进度完成情况集合
     */
    @GetMapping("/questInfoPage/{questId}")
    public Result<Collection<QuestScheduleCompletionVO>> getQuestInfoPageSchedules(@PathVariable Long questId) {
        return Result.ok(questScheduleService.getQuestInfoPageSchedule(questId));
    }

    /**
     * 任务详情页-地图坐标点
     * @param questId 任务ID
     * @return 地图坐标点集合
     */
    @GetMapping("/questInfoPage/map/{questId}")
    public Result<List<QuestScheduleMapPoint>> getQuestInfoPageMap(@PathVariable Long questId) {
        return Result.ok(questScheduleService.getQuestInfoPageMap(questId));
    }

    /**
     * 进度详情页-进度详细信息
     * @param id 进度ID
     * @return 进度详情
     */
    @GetMapping("/infoPage/{id}")
    public Result<QuestSchedulePageInfoVO> getInfoPage(@PathVariable Long id) {
        return Result.ok(questScheduleService.getQuestScheduleInfoPage(id));
    }

    /**
     * 最近任务进度
     * @param longitude 经度
     * @param latitude 维度
     * @return 小卡片
     */
    @GetMapping("/near")
    public Result<QuestCardVO> getNearest(Double longitude, Double latitude) {
        return Result.ok(questScheduleService.getNearest(longitude, latitude));
    }
}
