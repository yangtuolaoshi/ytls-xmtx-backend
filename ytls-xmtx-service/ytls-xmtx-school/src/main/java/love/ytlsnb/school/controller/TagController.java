package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytls.api.ad.TagClient;
import love.ytlsnb.model.ad.po.Tag;
import love.ytlsnb.model.ad.vo.TagVO;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/31 17:07
 */
@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagClient tagClient;

    @PostMapping
    public Result addTag(@RequestBody Tag tag) {
        log.info("添加标签:{}", tag);
        return tagClient.addTag(tag);
    }

    @DeleteMapping("/{tagId}")
    public Result deleteById(@PathVariable Long tagId) {
        log.info("根据ID删除标签");
        return tagClient.deleteById(tagId);
    }

    @PutMapping
    public Result updateTag(@RequestBody Tag tag) {
        log.info("修改标签:{}", tag);
        return tagClient.updateTag(tag);
    }

    @GetMapping("/{tagId}")
    public Result<TagVO> getTagVOById(@PathVariable Long tagId) {
        log.info("根据ID获取标签:{}", tagId);
        return tagClient.getTagVOById(tagId);
    }

    /**
     * 查询当前参数ID对应标签下的子标签（不包括本身）
     *
     * @param parentId 待查询的父标签ID
     * @return 子标签集合
     */
    @GetMapping("/list/{parentId}")
    public Result<List<TagVO>> listByParentId(@PathVariable Long parentId) {
        log.info("根据父ID查询标签:{}", parentId);
        return tagClient.listByParentId(parentId);
    }

    @GetMapping("/list")
    public Result<List<TagVO>> listAll() {
        log.info("获取所有标签");
        return tagClient.listAll();
    }
}
