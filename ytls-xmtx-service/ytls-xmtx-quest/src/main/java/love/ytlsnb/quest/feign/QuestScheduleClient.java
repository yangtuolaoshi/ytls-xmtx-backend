package love.ytlsnb.quest.feign;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleInfoVO;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;
import love.ytlsnb.quest.service.QuestScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务进度远程调用
 *
 * @author 金泓宇
 * @date 2024/3/8
 */
@RestController
@RequestMapping("/schedule")
@Slf4j
public class QuestScheduleClient {
    @Autowired
    private QuestScheduleService questScheduleService;

    /**
     * 管理平台地图展示
     * @return 坐标点
     */
    @GetMapping("/admin/map")
    public Result<List<QuestScheduleVo>> getAdminMap(MapFilterDTO mapFilterDTO, Long schoolId) {
        return Result.ok(questScheduleService.getAdminMap(mapFilterDTO, schoolId));
    }

    @PostMapping
    public Result<Long> add(@RequestBody QuestDTO questDTO) {
        return Result.ok(questScheduleService.add(questDTO));
    }

    @GetMapping("/{id}")
    public Result<QuestScheduleInfoVO> getInfoById(@PathVariable Long id) {
        return Result.ok(questScheduleService.getInfoById(id));
    }

    @GetMapping("/page/{questId}")
    public PageResult<List<QuestSchedule>> getPageByQuestId(@PathVariable Long questId, int page, int size) {
        return questScheduleService.getPageByQuestId(questId, page, size);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody QuestDTO questDTO) {
        // TODO 功能未完成
        return Result.ok(questScheduleService.update(questDTO));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return Result.ok(questScheduleService.deleteById(id));
    }
}
