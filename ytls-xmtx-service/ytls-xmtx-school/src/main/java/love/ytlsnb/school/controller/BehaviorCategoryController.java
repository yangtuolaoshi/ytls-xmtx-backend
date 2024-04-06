package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytls.api.ad.BehaviorCategoryClient;
import love.ytlsnb.model.ad.po.BehaviorCategory;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/4/4 21:50
 */
@Slf4j
@RestController
@RequestMapping("/behaviorCategory")
public class BehaviorCategoryController {
    @Autowired
    private BehaviorCategoryClient behaviorCategoryClient;

    @PostMapping
    public Result addBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory) {
        log.info("新增行为分类数据:{}", behaviorCategory);
        return behaviorCategoryClient.addBehaviorCategory(behaviorCategory);
    }

    @DeleteMapping("/{bcId}")
    public Result deleteBehaviorCategoryById(@PathVariable Long bcId) {
        log.info("根据ID删除行为分类数据:{}", bcId);
        return behaviorCategoryClient.deleteBehaviorCategoryById(bcId);
    }

    @PutMapping
    public Result updateBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory) {
        log.info("修改行为分类数据:{}", behaviorCategory);
        return behaviorCategoryClient.updateBehaviorCategory(behaviorCategory);
    }

    @GetMapping("/{bcId}")
    public Result<BehaviorCategory> getBehaviorCategoryById(@PathVariable Long bcId) {
        log.info("根据ID查询行为分类数据:{}", bcId);
        return behaviorCategoryClient.getBehaviorCategoryById(bcId);
    }
}

