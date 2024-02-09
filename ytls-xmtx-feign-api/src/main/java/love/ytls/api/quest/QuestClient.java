package love.ytls.api.quest;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.po.Quest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ula
 * @date 2024/2/7 16:49
 */
@FeignClient("quest-service")
public interface QuestClient {
    @PostMapping("/api/quest/quest")
    Result addQuest(@RequestBody Quest quest);
}
