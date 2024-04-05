package love.ytlsnb.ad.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.service.BehaviorCategoryService;
import love.ytlsnb.model.ad.po.BehaviorCategory;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/3/20 15:06
 */
@Slf4j
@RestController
@RequestMapping("/behaviorCategory")
public class BehaviorCategoryController {
    @Autowired
    private BehaviorCategoryService behaviorCategoryService;

    @PostMapping
    public Result addBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory) {
        log.info("新增行为分类数据:{}", behaviorCategory);
        behaviorCategoryService.addBehaviorCategory(behaviorCategory);
        return Result.ok();
    }

    @DeleteMapping("/{bcId}")
    public Result deleteBehaviorCategoryById(@PathVariable Long bcId) {
        log.info("根据ID删除行为分类数据:{}", bcId);
        behaviorCategoryService.removeById(bcId);
        return Result.ok();
    }

    @PutMapping
    public Result updateBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory) {
        log.info("修改行为分类数据:{}", behaviorCategory);
        behaviorCategoryService.updateBehaviorCategory(behaviorCategory);
        return Result.ok();
    }

    @GetMapping("/{bcId}")
    public Result<BehaviorCategory> getBehaviorCategoryById(@PathVariable Long bcId) {
        log.info("根据ID查询行为分类数据:{}", bcId);
        return Result.ok(behaviorCategoryService.getById(bcId));
    }
}
