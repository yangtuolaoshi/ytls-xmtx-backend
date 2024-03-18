package love.ytls.api.quest;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleInfoVO;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务进度有关远程调用
 *
 * @author 金泓宇
 * @date 2024/3/8
 */
@FeignClient(value = "quest-service", contextId = "quest-schedule")
public interface QuestScheduleClient {
    @GetMapping("/api/schedule/admin/map")
    Result<List<QuestScheduleVo>> getAdminMap(@SpringQueryMap MapFilterDTO mapFilterDTO, @RequestParam Long schoolId);

    @GetMapping("/api/schedule/page/{questId}")
    Result<List<QuestSchedule>> getPageByQuestId(
            @PathVariable Long questId,
            @RequestParam int page,
            @RequestParam int size
    );

    @GetMapping("/api/schedule/{id}")
    Result<QuestScheduleInfoVO> getInfoById(@PathVariable Long id);

    @PostMapping("/api/schedule")
    Result<Long> add(@RequestBody QuestDTO questDTO);

    @PutMapping("/api/schedule")
    Result<Boolean> update(@RequestBody QuestDTO questDTO);

    @DeleteMapping("/api/schedule/{id}")
    Result<Boolean> deleteById(@PathVariable Long id);
}
