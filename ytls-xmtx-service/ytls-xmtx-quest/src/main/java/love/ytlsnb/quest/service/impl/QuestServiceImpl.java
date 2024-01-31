package love.ytlsnb.quest.service.impl;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户基本信息业务层实现类
 *
 * @author ula
 * @date 2024/01/30
 */
@Service
@Slf4j
public class QuestServiceImpl implements QuestService {
    @Autowired
    private QuestMapper questMapper;
}
