package love.ytlsnb.quest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;

/**
 * 用户基本信息业务层
 *
 * @author ula
 * @date 2024/01/30
 */
public interface QuestService extends IService<Quest> {
    void insert(QuestInsertDTO questInsertDTO);
}
