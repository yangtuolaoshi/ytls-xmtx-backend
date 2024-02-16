package love.ytlsnb.quest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestVO;

/**
 * 用户基本信息业务层
 *
 * @author ula
 * @date 2024/01/30
 */
public interface QuestService extends IService<Quest> {
    void insert(QuestInsertDTO questInsertDTO);

    /**
     * @param schoolId 学校ID
     * @return 根据学校ID获得的Quest对象
     */
    QuestVO getRootQuest(Long schoolId);
}
