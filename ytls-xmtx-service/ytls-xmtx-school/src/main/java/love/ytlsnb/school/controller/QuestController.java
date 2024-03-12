package love.ytlsnb.school.controller;

import love.ytls.api.quest.QuestClient;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.OSSUtil;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 任务管理
 *
 * @author 金泓宇
 * @date 2024/3/8
 */
@RestController
@RequestMapping("/quest")
public class QuestController {
    @Autowired
    private QuestClient questClient;

    // TODO 任务服务里还留了一个上传图片
    // TODO 图片压缩
    // TODO 如果刚上传的图片又在前端取消了，后端能不能删除
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String newFilename = "quest/location/" + filename + UUID.randomUUID();
        OSSUtil.uploadFile(OSSUtil.QUEST_BUCKET, newFilename, file.getInputStream());
        String accessUrl = "https://"
                + OSSUtil.QUEST_BUCKET + "."
                + OSSUtil.END_POINT + "/"
                + newFilename;
        return Result.ok(accessUrl);
    }

    @GetMapping("/page")
    public PageResult<List<QuestVo>> getPageByCondition(QuestQueryDTO questQueryDTO, @RequestParam int page, @RequestParam int size) {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        questQueryDTO.setSchoolId(schoolId);
        return questClient.getPageByCondition(questQueryDTO, page, size);
    }

    @GetMapping("/all")
    public Result<List<Quest>> getAll() {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        return questClient.getAll(schoolId);
    }

    @GetMapping("/{id}")
    public Result<QuestInfoVo> getById(@PathVariable Long id) {
        return questClient.getById(id);
    }

    @PostMapping
    public Result<Long> add(@RequestBody QuestDTO questAddDTO) {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        questAddDTO.setSchoolId(schoolId);
        return questClient.add(questAddDTO);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody QuestDTO questDTO) {
        return questClient.update(questDTO);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        return questClient.deleteById(id, schoolId);
    }
}
