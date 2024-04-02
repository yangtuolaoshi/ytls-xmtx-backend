package love.ytlsnb.quest.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.*;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务表现层
 *
 * @author 金泓宇
 * @author 2024/2/29
 */
@RestController
@RequestMapping("/quest")
@Slf4j
public class QuestController {
    @Autowired
    private QuestService questService;

    /**
     * 任务详情页-任务信息
     * @param id 任务ID
     * @return 任务详细信息
     */
    @GetMapping("/infoPage/{id}")
    public Result<QuestInfoPageVO> getInfoPageById(@PathVariable Long id) {
        return Result.ok(questService.getQuestInfoById(id));
    }

    /**
     * 获取当前用户进行中的任务
     * @param type 任务类型
     * @return 进行中的任务列表
     */
    @GetMapping("/ongoing")
    public Result<List<QuestListItemVO>> getOngoingQuests(Integer type) {
        return Result.ok(questService.getOngoingQuests(type));
    }

    /**
     * 查询已经完成的任务
     * @param type 任务类型
     * @return 已经完成的任务列表
     */
    @GetMapping("/finished")
    public Result<List<QuestListItemVO>> getFinishedQuests(Integer type) {
        return Result.ok(questService.getFinishedQuests(type));
    }

    /**
     * 查询未开始的任务
     * @param type 任务类型
     * @return 未开始的任务列表
     */
    @GetMapping("/unstarted")
    public Result<List<QuestListItemVO>> getUnstartedQuests(Integer type) {
        return Result.ok();
    }
}
