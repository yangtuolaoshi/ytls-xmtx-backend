package love.ytls.api.quest;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 任务有关远程调用
 *
 * @author 金泓宇
 * @date 2024/3/7
 */
@FeignClient(value = "quest-service", contextId = "quest")
public interface QuestClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/quest/page")
    PageResult<List<QuestVo>> getPageByCondition(
            @SpringQueryMap QuestQueryDTO questQueryDTO,
            @RequestParam int page,
            @RequestParam int size
    );

    @GetMapping("/api/quest/all")
    Result<List<Quest>> getAll(@RequestParam Long schoolId);

    @GetMapping("/api/quest/{id}")
    Result<QuestInfoVo> getById(@PathVariable Long id);

    @PostMapping("/api/quest")
    Result<Long> add(@RequestBody QuestDTO questAddDTO);

    @PutMapping("/api/quest")
    Result<Boolean> update(@RequestBody QuestDTO questDTO);

    @DeleteMapping("/api/quest/{id}")
    Result<Boolean> deleteById(@PathVariable Long id, @RequestParam Long schoolId);
}
