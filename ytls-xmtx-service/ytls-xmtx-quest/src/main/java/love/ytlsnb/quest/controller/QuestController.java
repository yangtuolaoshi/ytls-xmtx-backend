package love.ytlsnb.quest.controller;


import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户基本信息控制器层
 *
 * @author ula
 * @date 2024/01/30
 */
@Slf4j
@RestController
@RequestMapping("/quest")
public class QuestController {
    @Autowired
    private QuestService userService;

    @PostMapping("quest")
    public Result addQuest(@RequestBody QuestInsertDTO questInsertDTO) {
        return Result.ok();
    }
}
