package love.ytls.api.ad;

import love.ytlsnb.model.ad.po.BehaviorCategory;
import love.ytlsnb.model.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/4/4 21:48
 */
@FeignClient(value = "ad-service",contextId = "behaviorCategory")
public interface BehaviorCategoryClient {

    @PostMapping("/api/behaviorCategory")
     Result addBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory) ;

    @DeleteMapping("/api/behaviorCategory/{bcId}")
     Result deleteBehaviorCategoryById(@PathVariable Long bcId);

    @PutMapping("/api/behaviorCategory")
     Result updateBehaviorCategory(@RequestBody BehaviorCategory behaviorCategory);

    @GetMapping("/api/behaviorCategory/{bcId}")
     Result<BehaviorCategory> getBehaviorCategoryById(@PathVariable Long bcId);
}
