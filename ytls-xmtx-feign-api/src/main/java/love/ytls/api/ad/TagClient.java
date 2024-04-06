package love.ytls.api.ad;

import love.ytlsnb.model.ad.po.Tag;
import love.ytlsnb.model.ad.vo.TagVO;
import love.ytlsnb.model.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/31 17:09
 */
@FeignClient(value = "ad-service",contextId = "tag")
public interface TagClient {
    @PostMapping("/api/tag")
    Result addTag(@RequestBody Tag tag);

    @DeleteMapping("/api/tag/{tagId}")
    Result deleteById(@PathVariable Long tagId);

    @PutMapping("/api/tag")
    Result updateTag(@RequestBody Tag tag);

    @GetMapping("/api/tag/{tagId}")
    Result<TagVO> getTagVOById(@PathVariable Long tagId);

    @GetMapping("/api/tag/list/{parentId}")
    Result<List<TagVO>> listByParentId(@PathVariable Long parentId);

    @GetMapping("/api/tag/list")
    Result<List<TagVO>> listAll();
}
