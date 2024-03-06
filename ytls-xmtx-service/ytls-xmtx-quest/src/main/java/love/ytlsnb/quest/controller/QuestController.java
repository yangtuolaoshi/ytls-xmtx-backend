package love.ytlsnb.quest.controller;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务表现层接口
 *
 * @author 金泓宇
 * @author 2024/2/29
 */
@RestController
@RequestMapping("/quest")
public class QuestController {
    @Autowired
    private QuestService questService;

    @GetMapping("/page")
    public PageResult<List<QuestVo>> getPageByCondition(QuestQueryDTO questQueryDTO, int page, int size) {
        return questService.getPageByCondition(questQueryDTO, page, size);
    }

    @PostMapping
    public Result<Long> add(@RequestBody QuestDTO questAddDTO) {
        return Result.ok(questService.add(questAddDTO));
    }

    @GetMapping("/{id}")
    public Result<QuestInfoVo> getById(@PathVariable Long id) {
        return Result.ok(questService.getInfoById(id));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody QuestDTO questDTO) {
        return Result.ok(questService.update(questDTO));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        return Result.ok(questService.deleteById(id));
    }
}
