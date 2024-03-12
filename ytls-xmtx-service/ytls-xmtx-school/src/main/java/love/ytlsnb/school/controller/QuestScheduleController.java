package love.ytlsnb.school.controller;

import love.ytls.api.quest.QuestScheduleClient;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleInfoVO;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class QuestScheduleController {
    @Autowired
    private QuestScheduleClient questScheduleClient;

    @GetMapping("/admin/map")
    public Result<List<QuestScheduleVo>> getAdminMap(MapFilterDTO mapFilterDTO) {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        return questScheduleClient.getAdminMap(mapFilterDTO, schoolId);
    }

    @GetMapping("/page/{questId}")
    public Result<List<QuestSchedule>> getPageByQuestId(@PathVariable Long questId, int page, int size) {
        return questScheduleClient.getPageByQuestId(questId, page, size);
    }

    @GetMapping("/{id}")
    public Result<QuestScheduleInfoVO> getInfoById(@PathVariable Long id) {
        return questScheduleClient.getInfoById(id);
    }

    @PostMapping
    public Result<Long> add(@RequestBody QuestDTO questDTO) {
        return questScheduleClient.add(questDTO);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody QuestDTO questDTO) {
        return questScheduleClient.update(questDTO);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return questScheduleClient.deleteById(id);
    }
}
