package love.ytlsnb.ad.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.service.TagService;
import love.ytlsnb.model.ad.po.Tag;
import love.ytlsnb.model.ad.vo.TagVO;
import love.ytlsnb.model.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:47
 */
@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping
    public Result addTag(@RequestBody Tag tag) {
        log.info("新增标签:{}", tag);
        tagService.addTag(tag);
        return Result.ok();
    }

    @DeleteMapping("/{tagId}")
    public Result deleteById(@PathVariable Long tagId) {
        log.info("根据ID删除标签");
        tagService.deleteById(tagId);
        return Result.ok();
    }

    @PutMapping
    public Result updateTag(@RequestBody Tag tag) {
        log.info("修改标签:{}", tag);
        tagService.updateTag(tag);
        return Result.ok();
    }

    @GetMapping("/{tagId}")
    public Result<TagVO> getTagVOById(@PathVariable Long tagId) {
        log.info("根据ID获取标签:{}", tagId);
        TagVO byId = tagService.getTagVOById(tagId);
        return Result.ok(byId);
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
        List<TagVO> tagList = tagService.listByParentId(parentId);
        return Result.ok(tagList);
    }

    @GetMapping("/list")
    public Result<List<TagVO>> listAll() {
        log.info("获取所有标签");
        List<TagVO> tagList = tagService.listAll();
        return Result.ok(tagList);
    }
}
